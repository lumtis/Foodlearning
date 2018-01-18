#!/bin/bash

sudo apt install -y zookeeper zookeeperd

sudo cp ~/zoo.cfg /etc/zookeeper/conf/zoo.cfg
sudo cp ~/myid /etc/zookeeper/conf/myid

sudo service zookeeper restart
