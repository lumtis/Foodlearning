echo "START SCRIPT LAUNCH KAFKA\n" > ~/logs.txt

/opt/Kafka/kafka_2.10-0.10.0.1/bin/zookeeper-server-start.sh /opt/Kafka/kafka_2.10-0.10.0.1/config/zookeeper.properties &
/opt/Kafka/kafka_2.10-0.10.0.1/bin/kafka-server-start.sh /opt/Kafka/kafka_2.10-0.10.0.1/config/server.properties &
/opt/Kafka/kafka_2.10-0.10.0.1/bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic TOPICINGREDIENTS;

echo 'SCRIPT LAUNCH KAFKA EXECUTED' >> ~/logs.txt
