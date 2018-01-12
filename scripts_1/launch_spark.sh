echo "START SCRIPT LAUNCH SPARK\n" > ~/logs.txt

./spark-2.2.0-bin-hadoop2.7/bin/spark-submit --jars ./Foodlearning/spark/kafka.jar --packages datastax:spark-cassandra-connector:2.0.6-s_2.11 ./Foodlearning/spark/foodlearning.py

echo 'SCRIPT LAUNCH SPARK EXECUTED' >> ~/logs.txt
