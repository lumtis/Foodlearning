echo "START SCRIPT INSTALLATION ALL\n" > ~/logs.txt

./Foodlearning/scripts_1/install_api.sh
./Foodlearning/scripts_1/install_kafka.sh
./Foodlearning/scripts_1/install_spark.sh
./Foodlearning/scripts_1/install_cassandra.sh
./Foodlearning/scripts_1/install_akka.sh

echo 'SCRIPT INSTALLATION ALL EXECUTED' >> ~/logs.txt
