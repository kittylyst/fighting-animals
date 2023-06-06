/* Copyright (C) Red Hat 2023 */
package io.opentelemetry.examples.animal;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.logging.LoggingMeterRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AnimalApplication {

  @Bean
  public MeterRegistry basicRegistry() {
    return new LoggingMeterRegistry();
  }

  public static void main(String[] args) {
    SpringApplication.run(AnimalApplication.class, args);
  }
}
