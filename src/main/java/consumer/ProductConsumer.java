package consumer;

import com.hortonworks.registries.schemaregistry.client.SchemaRegistryClient;
import com.hortonworks.registries.schemaregistry.serdes.avro.kafka.KafkaAvroDeserializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import schema.Product;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

public class ProductConsumer {
    private final Properties config = new Properties();

    public ProductConsumer() {
        String bootstrapServers = "localhost:9091";
        String schemaRegistryURL = "http://localhost:9090/api/v1";

        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(SchemaRegistryClient.Configuration.SCHEMA_REGISTRY_URL.name(), schemaRegistryURL);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class.getName());
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class.getName());
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "productConsumer");
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    }

    public void consume() {
        AtomicBoolean doConsume = new AtomicBoolean(true);

        try(KafkaConsumer<Integer, Product> consumer = new KafkaConsumer<>(config)) {
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("Shutting down gracefully ...");
                doConsume.set(false);
            }));

            consumer.subscribe(Collections.singleton("product"));

            while(doConsume.get()) {
                ConsumerRecords<Integer, Product> records = consumer.poll(Duration.ofMillis(1000));
                for (var record : records) {
                    System.out.println("Found " + record.value() + " at offset " + record.offset());
                }
            }
        }
        finally {
            System.out.println("Done.");
        }

    }

    public static void main(String[] args) {
        ProductConsumer consumer = new ProductConsumer();

        consumer.consume();
    }
}
