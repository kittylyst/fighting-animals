/* Copyright (C) Red Hat 2023-2024 */
package io.opentelemetry.examples.feline;

import java.util.List;
import java.util.Properties;
import java.util.UUID;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FelineController {
  private final List<String> CATS = List.of("tabby", "jaguar", "leopard");

  private final KafkaProducer<String, String> producer;

  public FelineController() {
    Properties properties = new Properties();
    properties.put("bootstrap.servers", "kafka-1:9092"); // PLAINTEXT
    properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    producer = new KafkaProducer<>(properties);
  }

  @GetMapping("/getAnimal")
  public String makeBattle() throws InterruptedException {
    // Random pause
    Thread.sleep((int) (20 * Math.random()));

    // Return random cat (and also send to Kafka)
    var cat = CATS.get((int) (CATS.size() * Math.random()));
    var key = UUID.randomUUID().toString();
    var producerRecord = new ProducerRecord<>("FELINE", key, cat);
    producer.send(producerRecord);
    return cat;
  }
}
