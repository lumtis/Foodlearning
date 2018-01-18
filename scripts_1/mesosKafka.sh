sudo apt-get update
sudo apt-get -y upgrade


#install JDK 7
sudo apt-get install openjdk-7-jre



#install gradle
wget https://services.gradle.org/distributions/gradle-4.1-bin.zip
sudo mkdir /opt/gradle
sudo apt-get install unzip
sudo unzip -d /opt/gradle gradle-3.4.1-bin.zip
export PATH=$PATH:/opt/gradle/gradle-3.4.1/bin


#install kafka-mesos connector
git clone https://github.com/mesos/kafka
cd kafka
./gradlew jar downloadKafka
export MESOS_NATIVE_JAVA_LIBRARY=/usr/local/lib/libmesos.so
export LIBPROCESS_IP=<IP_ACCESSIBLE_FROM_MASTER>
touch kafka-mesos.properties
echo 'storage=zk:/kafka-mesos
master=zk://10.0.0.30:2181/mesos
zk=10.0.0.30:2181,10.0.0.31:2181,10.0.0.32:2181/KafkaCluster
api=http://10.0.0.30:7000' >> kafka-mesos.properties





