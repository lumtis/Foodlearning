#Partie exécution
cd /opt/Kafka/kafka_2.10-0.10.0.1/
bin/zookeeper-server-start.sh config/zookeeper.properties &
bin/kafka-server-start.sh config/server.properties &
bin/kafka-topics.sh --create --config min.insync.replicas=3 --config max.message.bytes=47185920 --config unclean.leader.election.enable=false --topic TOPICINGREDIENTS --partitions 1 --replication-factor 3;
#bin/kafka-console-consumer.sh --zookeeper localhost:2181 --from-beginning --topic TOPICINGREDIENTS;
