echo "START SCRIPT LAUNCH ALL\n" > ~/logs.txt

./Foodlearning/scripts_1/launch_api.sh
./Foodlearning/scripts_1/launch_kafka.sh
./Foodlearning/scripts_1/launch_spark.sh
./Foodlearning/scripts_1/launch_cassandra.sh
./Foodlearning/scripts_1/launch_akka.sh

echo 'SCRIPT LAUNCH ALL EXECUTED' >> ~/logs.txt
