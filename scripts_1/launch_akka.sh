echo "START SCRIPT LAUNCH AKKA\n" > ~/logs.txt

cd ./Foodlearning
sbt run > ~/log_akka.txt

echo 'SCRIPT LAUNCH AKKA EXECUTED' >> ~/logs.txt
