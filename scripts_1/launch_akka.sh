echo "START SCRIPT LAUNCH AKKA\n" > ~/logs.txt

cd ./Foodlearning
sbt run

echo 'SCRIPT LAUNCH AKKA EXECUTED' >> ~/logs.txt
