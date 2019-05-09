# Simple Proof of Concept to show Apache Kafka and Neo4j

## Requirements

* Java 8
* Gradle
* Access to Maven packages in the build.gradle
* Apache Kafka, Apache ZooKeeper, and Neo4j running (Dockerfile coming soon)

### Fake Producing data into Kafka

* Fake authors and posts data is provided in the `resources/fake/data.json`
* `Producer.java` produces the fake data on a particular topics, and key 

### Getting Started

* Check out `Main.java`

### Consumers

Coming Soon

# Helpful CLI Commands below

Spawn ZooKeeper, default configuration, on port 2181

`zookeeper-server-start config/zookeeper.properties`

Spawn Kafka, defaulting configuration, on port 9092

`kafka-server-start config/server.properties`

## Create Topics we need (running locally for 1 replication factor)

Create the Author Topic.

`kafka-topics --zookeeper 127.0.0.1:2181 --topic {AUTHOR_TOPIC} --create --partitions 6 --replication-factor 1`

Create the Post Topic.

`kafka-topics --zookeeper 127.0.0.1:2181 --topic {POST_TOPIC} --create --partitions 6 --replication-factor 1`

* {AUTHOR_TOPIC} - default is `author_topic`
    * The default key that will be published to is in the form `id_{AUTHOR_ID}` 
* {POST_TOPIC} - default is  `post_topic`