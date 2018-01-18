#!/bin/bash
echo "START SCRIPT INSTALLATION MESOS\n" > install_mesos.txt
sudo apt -y update 
sudo DEBIAN_FRONTEND=noninteractive apt-get -y -o Dpkg::Options::="--force-confdef" -o Dpkg::Options::="--force-confold" dist-upgrade
echo "update and upgrade done\n" >> install_mesos.txt

sudo apt -y install tar wget git openjdk-8-jdk autoconf libtool build-essential python-dev python-six python-virtualenv libcurl4-nss-dev libsasl2-dev libsasl2-modules maven libapr1-dev libsvn-dev zlib1g-dev
echo "installation openjdk done\n" >> install_mesos.txt

cd /home/ubuntu
wget http://www.apache.org/dist/mesos/1.4.1/mesos-1.4.1.tar.gz
tar -zxf mesos-1.4.1.tar.gz
cd mesos-1.4.1
mkdir build
cd build
../configure
core=`grep -c processor /proc/cpuinfo`
echo "configure done, next step : make -j$core\n" >> install_mesos.txt

make -j$core
echo "SCRIPT INSTALLATION MESOS EXECUTED" >> install_mesos.txt
