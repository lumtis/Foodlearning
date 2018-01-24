# pip install boto3
# pip install paramiko
# pip install scp
# pip install awscli

# First, sign up to aws and subscribe to free services (phone call etc.)

# Install AWS command line tools
# Setup AWS credentials with command :
# $aws configure
# ACCESS KEY ID et ACCESS SECRET KEY : create key pairs from account settings
# DEFAULT REGION NAME : read default config from the console (automatically log after the phone call)

# If it fails, be sure that port 22 are open on the instance

import sys
import time
import boto3
import boto3.ec2
import paramiko
from scp import SCPClient
import os

KEYPAIR_NAME = "" # READ THIS: http://docs.aws.amazon.com/AWSEC2/latest/UserGuide/ec2-key-pairs.html#having-ec2-create-your-key-pair
KEY_FILE = "" # generated with ec2 key pairs
LOCAL_PATH = ""
SCRIPT_DEPLOY = ""
SCRIPT_UPLOAD = ""
# CENTOS_AMI = "ami-597d553c" #check your AMI version according to the location ami-db715bbe
CENTOS_AMI = "ami-db715bbe"
INSTANCE_TYPE = "t2.large"
# SECURITY_GROUP_ID = "sg-1b9eb473" # Read it from your console / default config
# AVAILABILITY_ZONE = "us-east-2a" # check from console availability (Same location as the one in aws configure + 'a', 'b' or 'c')
PUBLICKEY_NAME = 'id_rsa_mainvpc.pub'
IP_MASTER = ['10.0.0.30', '10.0.0.31', '10.0.0.32']
IP_SLAVE = ['10.0.1.50', '10.0.1.51', '10.0.1.52', '10.0.1.53', '10.0.1.54', '10.0.1.55', '10.0.1.56', '10.0.1.57', '10.0.1.58']
public_ip_master = []
main_machine_ip = None

def upload_key_and_chmod(ip):
    ssh_client = paramiko.SSHClient()
    ssh_client.set_missing_host_key_policy(paramiko.AutoAddPolicy())

    print "Connecting to " + ip
    ssh_client.connect(ip,
                       username="ubuntu",
                       key_filename=KEY_FILE)

    print "Uploading key"
    scp = SCPClient(ssh_client.get_transport())
    scp.put(KEY_FILE, '~/.ssh/')
    scp.close()
    time.sleep(10)

    print "Change script access permissions"
    command = 'tar xzvf deploy.tgz'
    (stdin, stdout, stderr) = ssh_client.exec_command(command)

    command = "chmod 0400 ~/.ssh/key.pem"
    (stdin, stdout, stderr) = ssh_client.exec_command(command)
    chmod_return = stdout.channel.recv_exit_status()

    if (chmod_return != 0):
        eprint("Changing access permissions failed")
        print_error_and_exit(stderr, ssh_client)

    print "Closing connection\n"
    ssh_client.close()

def upload_script_and_run(ip):
    ssh_client = paramiko.SSHClient()
    ssh_client.set_missing_host_key_policy(paramiko.AutoAddPolicy())

    print "Connecting to " + ip
    ssh_client.connect(ip,
                       username="ubuntu",
                       key_filename=KEY_FILE)

    print "Uploading script"
    scp = SCPClient(ssh_client.get_transport())
    scp.put(SCRIPT_UPLOAD)

    scp.close()
    time.sleep(10)

    print "Change script access permissions"
    command = 'tar xzvf deploy.tgz'
    (stdin, stdout, stderr) = ssh_client.exec_command(command)

    command = 'bash deploy.sh'
    (stdin, stdout, stderr) = ssh_client.exec_command(command)

    print "Executing script"
    command = "./" + str(os.path.basename(SCRIPT_DEPLOY))
    (stdin, stdout, stderr) = ssh_client.exec_command(command)

    print "Closing connection\n"
    ssh_client.close()

def print_error_and_exit(stderr, ssh_client):
    for line in stderr.readlines():
        eprint(line)

    print "Closing connection"
    ssh_client.close()

    exit(1)

def create_vm(local_ip):
    if local_ip in IP_MASTER:
        subnet = subnet_public
    else:
        subnet = subnet_private

    ec2_resource = subnet

    print "Creating instance"
    print("subnet: " + str(subnet))
    instances = ec2_resource.create_instances(ImageId=CENTOS_AMI,
                                              InstanceType=INSTANCE_TYPE,
                                              SecurityGroupIds=[sg.id],
                                              # SecurityGroupIds=[SECURITY_GROUP_ID],
                                              PrivateIpAddress=local_ip,
                                              KeyName=KEYPAIR_NAME,
                                              # Placement={'AvailabilityZone': AVAILABILITY_ZONE},
                                              MinCount=1,
                                              MaxCount=1)
    print("instances: " + str(instances))
    instance = instances[0]
    print "Instance ID: " + instance.id

    print "Waiting for instance startup"
    instance.wait_until_running()
    print "Instance running"

    if local_ip in IP_MASTER:
        print "Allocating static IP"
        addr = ec2_client.allocate_address(Domain='vpc')
        print("addr: " + str(addr))
        public_ip = addr["PublicIp"]
        print "IP: " + public_ip
        print "Associate IP with instance"
        ec2_client.associate_address(InstanceId=instance.id, PublicIp=public_ip)

        return addr

    else:
        return

def eprint(message):
    sys.stderr.write(message)
    sys.stderr.write("\r\n")

def configure_vpc():

    internet_gateway = ec2.create_internet_gateway()
    internet_gateway.attach_to_vpc(VpcId=vpc.vpc_id)

    route_table_public = vpc.create_route_table()
    route_ig_ipv4 = route_table_public.create_route(DestinationCidrBlock='0.0.0.0/0', GatewayId=internet_gateway.internet_gateway_id)

    route_table_public.associate_with_subnet(SubnetId=subnet_public.id)

    print "Allocating static IP to gateway"
    addr = ec2_client.allocate_address(Domain='vpc')
    print("addr: " + str(addr))
    public_ip = addr["PublicIp"]
    print "IP: " + public_ip
    print "Creation NAT gateway"
    gateway_nat = ec2_client.create_nat_gateway(AllocationId=addr['AllocationId'], SubnetId=subnet_public.id)
    time.sleep(120)
    route_table_private = vpc.create_route_table()
    route_ig_ipv4 = route_table_private.create_route(DestinationCidrBlock='0.0.0.0/0',
                                                     GatewayId=gateway_nat['NatGateway']['NatGatewayId'])

    route_table_private.associate_with_subnet(SubnetId=subnet_private.id)

    ip_ranges = [{
        'CidrIp': '0.0.0.0/0'
    }]

    perms = [{
        'IpProtocol': 'TCP',
        'FromPort': 80,
        'ToPort': 80,
        'IpRanges': ip_ranges,
    }, {
        'IpProtocol': 'TCP',
        'FromPort': 443,
        'ToPort': 443,
        'IpRanges': ip_ranges,
    }, {
        'IpProtocol': 'TCP',
        'FromPort': 22,
        'ToPort': 22,
        'IpRanges': ip_ranges,
    }, {
        'IpProtocol': 'icmp',
        'FromPort': 8,
        'ToPort': 0,
        'IpRanges': ip_ranges,
    }, {
        'IpProtocol': 'tcp',
        'FromPort': 5050,
        'ToPort': 5050,
        'IpRanges': ip_ranges,
    }, {
        'IpProtocol': 'tcp',
        'FromPort': 5051,
        'ToPort': 5051,
        'IpRanges': ip_ranges,
    }, {
        'IpProtocol': 'tcp',
        'FromPort': 2888,
        'ToPort': 2888,
        'IpRanges': ip_ranges,
    }, {
        'IpProtocol': 'tcp',
        'FromPort': 2181,
        'ToPort': 2181,
        'IpRanges': ip_ranges,
    }, {
        'IpProtocol': 'tcp',
        'FromPort': 3888,
        'ToPort': 3888,
        'IpRanges': ip_ranges,
    }, {
        'IpProtocol': 'tcp',
        'FromPort': 9093,
        'ToPort': 9093,
        'IpRanges': ip_ranges,
    }, {
        'IpProtocol': 'tcp',
        'FromPort': 199,
        'ToPort': 199,
        'IpRanges': ip_ranges,
    }, {
        'IpProtocol': 'tcp',
        'FromPort': 7000,
        'ToPort': 7000,
        'IpRanges': ip_ranges,
    }, {
        'IpProtocol': 'tcp',
        'FromPort': 7001,
        'ToPort': 7001,
        'IpRanges': ip_ranges,
    }, {
        'IpProtocol': 'tcp',
        'FromPort': 9160,
        'ToPort': 9160,
        'IpRanges': ip_ranges,
    }, {
        'IpProtocol': 'tcp',
        'FromPort': 9042,
        'ToPort': 9042,
        'IpRanges': ip_ranges,
    }, {
        'IpProtocol': 'tcp',
        'FromPort': 8080,
        'ToPort': 8080,
        'IpRanges': ip_ranges,
    }, ]

    sg.authorize_ingress(IpPermissions=perms)

    return vpc

def allow_ssh_connection(ip_temp):
    ssh_client = paramiko.SSHClient()
    ssh_client.set_missing_host_key_policy(paramiko.AutoAddPolicy())

    print "Connecting to temporary ip " + ip_temp
    ssh_client.connect(ip_temp,
                       username="ubuntu",
                       key_filename=KEY_FILE)

    print "Put public key to vpc machine"
    scp = SCPClient(ssh_client.get_transport())
    filepath = LOCAL_PATH + PUBLICKEY_NAME
    scp.put(filepath)

    scp.close()
    time.sleep(10)

    command = 'cat ' + PUBLICKEY_NAME + ' >> ~/.ssh/authorized_keys'
    (stdin, stdout, stderr) = ssh_client.exec_command(command)

    print "Closing connection\n"
    ssh_client.close()


if __name__ == "__main__":
    ec2 = boto3.resource('ec2')
    ec2_client = boto3.client("ec2")

    vpc = ec2.create_vpc(CidrBlock='10.0.0.0/16')
    sg = vpc.create_security_group(GroupName="VPC-SDTD", Description="VPC created and used to run the application")
    subnet_public = vpc.create_subnet(CidrBlock='10.0.0.0/24')
    subnet_private = vpc.create_subnet(CidrBlock='10.0.1.0/24')

    print("vpc configuration..")
    configure_vpc()
    print("vpc configured !")

    print("allocating slave machines")
    for local_ip in IP_SLAVE:
        ip = create_vm(local_ip)

    print("allocating master machines")
    master_has_executed_script = False
    for local_ip in IP_MASTER:
        ip = create_vm(local_ip)
        public_ip = ip["PublicIp"]
        public_ip_master.append(public_ip)

    print("trying to execute script")
    for public_ip in public_ip_master:
        if not master_has_executed_script:
            try:
                time.sleep(30)
                upload_key_and_chmod(public_ip)
                upload_script_and_run(public_ip)
                print('To connect manually :\nssh -i "~/.ssh/key.pem" ubuntu@' + str(public_ip))
                print('To connect manually from the main vpc machine:\nssh -i "~/.ssh/key.pem" ubuntu@10.0.1.xx')
                master_has_executed_script = True
                continue
            except:
                print('ERROR : master crashed !\nTrying on another master..')

    if not master_has_executed_script:
        print('ERROR : EVERY MASTER CRASHED\nExiting..')

    print("PUBLIC IP: " + str(public_ip_master))
