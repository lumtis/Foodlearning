echo "START SCRIPT INSTALLATION API\n" > ~/logs.txt

git clone https://github.com/cgaunet/SDTD.git
cd SDTD

sudo apt-get install openjdk-7-jdk
sudo unzip apache-maven-3.5.2-bin.zip
sudo mv apache-maven-3.5.2/ /opt/maven
sudo ln -s /opt/maven/bin/mvn /usr/bin/mvn
sudo cp maven.sh /etc/profile.d/maven.sh
sudo chmod +x /etc/profile.d/maven.sh
source /etc/profile.d/maven.sh

echo 'SCRIPT INSTALLATION API EXECUTED' >> ~/logs.txt
