import sys
reload(sys)
sys.setdefaultencoding('utf8')
import json
from pyspark import SparkContext, SparkConf
from pyspark.sql import SQLContext
from pyspark.streaming.kafka import KafkaUtils
from pyspark.streaming import StreamingContext


sc = SparkContext()
sqlContext = SQLContext(sc)
ssc = StreamingContext(sc, 10)

ings = sc.parallelize([])
ingsMap = {}
coefs = sc.parallelize([])


def find_between(s, first, last):
    try:
        start = s.rindex(first) + len(first)
        end = s.index(last, start)
        return s[start:end]
    except ValueError:
        return ""

def splitPair(str):
    couples = []
    ingsList = str.split(',')
    for i in range(0, len(ingsList)):
        for j in range(0, len(ingsList)):
            if (i != j):
                couple = (ingsList[i], ingsList[j])
                couples.append(couple)
    return couples


def updateIngs(rdd):
    global ings
    global ingsMap
    if not rdd.isEmpty():
        ings = ings.union(rdd).reduceByKey(lambda x,y : x+y)
        ingsMap = ings.collectAsMap()


def updatePairs(rdd):
    global ings
    global ingsMap
    if not rdd.isEmpty():
        newCoef = rdd.map(lambda ((a, b), n): (a, b, float(n)/float(ingsMap[a])))
        table = newCoef.toDF(["ing1", "ing2", "coef"])
        #table.show(50, False)
        table.write.format("org.apache.spark.sql.cassandra").mode('append').options(table="pairs", keyspace="foodlearning").save()


kvs = KafkaUtils.createStream(ssc, 'localhost:2181', "spark-streaming", {'TOPICINGREDIENTS': 1})


# Get the first array of ingredient and remove all spaces between ingredient
data = kvs.map(lambda x: x[1]).map(lambda s: find_between(s, '[', ']')).map(lambda s: s.strip())

newIngs = data.flatMap(lambda line: line.split(',')).map(lambda ing: (ing, 1)).reduceByKey(lambda a, b: a + b)
newPairs = data.flatMap(lambda line: splitPair(line)).map(lambda pair: (pair, 1)).reduceByKey(lambda a, b: a + b)

newIngs.foreachRDD(updateIngs)
newPairs.foreachRDD(updatePairs)

ssc.start()
ssc.awaitTermination()


##############################################
# Commands :
# CREATE KEYSPACE "foodlearning" WITH replication = {'class':'SimpleStrategy', 'replication_factor' : 3};
# CREATE TABLE pairs(ing1 text, ing2 text, coef float, PRIMARY KEY(ing1, ing2));
#  --jars kafka.jar
#  --packages datastax:spark-cassandra-connector:1.6.10-s_2.11
# ./spark-2.2.0-bin-hadoop2.7/bin/spark-submit --jars kafka.jar --packages datastax:spark-cassandra-connector:2.0.6-s_2.11 ./foodlearning.py
