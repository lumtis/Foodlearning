echo "START SCRIPT LAUNCH KAFKA\n" > ~/logs.txt

echo '' > ~/log_kafka.txt

sudo /opt/Kafka/kafka_2.10-0.10.0.1/bin/zookeeper-server-start.sh /opt/Kafka/kafka_2.10-0.10.0.1/config/zookeeper.properties & >> ~/log_kafka.txt
sudo /opt/Kafka/kafka_2.10-0.10.0.1/bin/kafka-server-start.sh /opt/Kafka/kafka_2.10-0.10.0.1/config/server.properties & >> ~/log_kafka.txt
sudo /opt/Kafka/kafka_2.10-0.10.0.1/bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic TOPICINGREDIENTS; >> ~/log_kafka.txt

echo 'SCRIPT LAUNCH KAFKA EXECUTED' >> ~/logs.txt
