echo "START SCRIPT LAUNCH API\n" > ~/logs.txt

mvn compile install exec:java > ~/log_api.txt

echo 'SCRIPT LAUNCH CASSANDRA EXECUTED' >> ~/logs.txt
