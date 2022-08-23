# Readme

Compile with

    mvn clean package

Run with

    java -cp target/HortonworksSchema-1.0-SNAPSHOT-jar-with-dependencies.jar producer.ProductProducer

You need both ZooKeeper/Kafka running, the latter on localhost:9091.
You'll also need to have Hortonworks Schema Registry running on port http://localhost:9090/api/v1


