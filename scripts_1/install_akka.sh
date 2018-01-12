echo "START SCRIPT INSTALLATION AKKA\n" > ~/logs.txt

sudo apt-get update -y

curl -sL https://deb.nodesource.com/setup_8.x | sudo -E bash -
sudo apt-get install -y nodejs

sudo apt-get -y install default-jre
sudo apt-get -y install default-jdk

echo "deb https://dl.bintray.com/sbt/debian /" | sudo tee -a /etc/apt/sources.list.d/sbt.list
sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 2EE0EA64E40A89B84B2DF73499E82A75642AC823
sudo apt-get update -y
sudo apt-get -y install sbt

# sudo apt-get -y install git
# git clone https://github.com/ltacker/Foodlearning

echo 'SCRIPT INSTALLATION AKKA EXECUTED' >> ~/logs.txt
