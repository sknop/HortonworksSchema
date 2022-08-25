# Readme

Compile with

    mvn clean package

Run with

    java -cp target/HortonworksSchema-1.0-SNAPSHOT-jar-with-dependencies.jar producer.ProductProducer

You need both ZooKeeper/Kafka running, the latter on localhost:9091.
You'll also need to have Hortonworks Schema Registry running on port http://localhost:9090/api/v1

You can use this test in conjunction with kafka_transactions (https://github.com/sknop/kafka_transactions) to try out various combinations of HW and CP schema registries.

