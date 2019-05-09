# Spawn ZooKeeper : 2181
zookeeper-server-start.bat config/zookeeper.properties

# Spawn Kafka : 9092
kafka-server-start.bat config/server.properties



# Topics
kafka-topics.bat --zookeeper 127.0.0.1:2181 --list
kafka-topics.bat --zookeeper 127.0.0.1:2181 --topic second_topic --describe

kafka-topics.bat --zookeeper 127.0.0.1:2181 --topic second_topic --create --partitions 6 --replication-factor 1


# Producers
kafka-console-producer.bat --broker-list 127.0.0.1:9092 --topic first_topic --producer-property acks=all


# Consumption from Shell
kafka-console-consumer.bat --bootstrap-server 127.0.0.1:9092 --topic first_topic --from-beginning

## Groups of Consumers
kafka-console-consumer.bat --bootstrap-server 127.0.0.1:9092 --topic first_topic --group my-first-application