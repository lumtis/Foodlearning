#!/bin/sh
# Partie Installation -- UBUNTU 16.04 LTS
echo "START SCRIPT INSTALLATION KAFKA\n" > ~/logs.txt

sudo apt-get update -y
sudo apt-get dist-upgrade -y
echo "update and upgrade done\n" >> install_kafka.txt

#sudo add-apt-repository -y ppa:webupd8team/java
#sudo apt-get update -y
#sudo apt-get install oracle-java8-installer -y
sudo add-apt-repository "deb http://ppa.launchpad.net/webupd8team/java/ubuntu xenial main"
sudo apt-get update
echo "oracle-java8-installer shared/accepted-oracle-license-v1-1 select true" | sudo debconf-set-selections
sudo apt-get -y --allow-unauthenticated install oracle-java8-set-default
#sudo java -version
echo "installation oracle done\n" >> install_kafka.txt

sudo apt-get -y install zookeeper
echo "installation zookeeper done\n" >> install_kafka.txt

wget https://archive.apache.org/dist/kafka/0.10.0.1/kafka_2.10-0.10.0.1.tgz
sudo mkdir /opt/Kafka
sudo mv kafka_2.10-0.10.0.1.tgz /opt/Kafka
cd /opt/Kafka
sudo tar xvf kafka_2.10-0.10.0.1.tgz
#sudo  /opt/Kafka/kafka_2.10-0.10.0.1/bin/kafka-server-start.sh /opt/Kafka/kafka_2.10-0.10.0.1/config/server.properties

echo 'SCRIPT INSTALLATION KAFKA EXECUTED' >> ~/logs.txt
