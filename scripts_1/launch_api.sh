echo "START SCRIPT LAUNCH API\n" > ~/logs.txt

mvn compile install exec:java

echo 'SCRIPT LAUNCH CASSANDRA EXECUTED' >> ~/logs.txt
