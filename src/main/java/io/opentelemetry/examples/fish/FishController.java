/* Copyright (C) Red Hat 2023-2024 */
package io.opentelemetry.examples.fish;

import java.util.List;
import java.util.Properties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FishController {
  private static final List<String> FISH = List.of("salmon", "cod", "turbot");

  private final KafkaProducer<String, String> producer;

  public FishController() {
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
    // Return random fish
    var fish = FISH.get((int) (FISH.size() * Math.random()));
    return fish;
  }
}
