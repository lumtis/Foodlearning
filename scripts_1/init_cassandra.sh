# Add pairs table in cassandra database
cqlsh -e "CREATE KEYSPACE foodlearning WITH replication = {'class':'SimpleStrategy', 'replication_factor' : 3};USE foodlearning;CREATE TABLE pairs(ing1 text, ing2 text, coef float, PRIMARY KEY(ing1, ing2));"
