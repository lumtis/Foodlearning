echo "START SCRIPT LAUNCH SPARK\n" > ~/logs.txt

./spark-2.2.1-bin-hadoop2.7/bin/spark-submit --master mesos://10.0.0.30:5050 --deploy-mode cluster --jars ./Foodlearning/spark/kafka.jar --packages datastax:spark-cassandra-connector:2.0.6-s_2.11 ./Foodlearning/spark/foodlearning.py > ~/log_spark.txt

echo 'SCRIPT LAUNCH SPARK EXECUTED' >> ~/logs.txt
