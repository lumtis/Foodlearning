#!/bin/bash

echo "START SCRIPT INSTALLATION CASSANDRA\n" > ~/logs.txt

sudo apt-get install curl

echo "deb http://www.apache.org/dist/cassandra/debian 311x main" | sudo tee -a /etc/apt/sources.list.d/cassandra.sources.list

curl https://www.apache.org/dist/cassandra/KEYS | sudo apt-key add -

sudo apt-get update
sudo apt-get -y install cassandra
sudo service cassandra start

echo 'SCRIPT INSTALLATION CASSANDRA EXECUTED' >> ~/logs.txt
