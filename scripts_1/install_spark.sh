echo "START SCRIPT INSTALLATION SPARK\n" > ~/logs.txt

sudo apt-get update -y

sudo apt-get -y install default-jdk
sudo apt-get -y install scala
sudo apt-get -y install git

wget http://apache.crihan.fr/dist/spark/spark-2.2.1/spark-2.2.1-bin-hadoop2.7.tgz

tar xvf spark-2.2.1-bin-hadoop2.7.tgz

echo 'SCRIPT INSTALLATION SPARK EXECUTED' >> ~/logs.txt
