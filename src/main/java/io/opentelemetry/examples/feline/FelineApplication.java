/* Copyright (C) Red Hat 2023-2024 */
package io.opentelemetry.examples.feline;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.logging.LoggingMeterRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class FelineApplication {

  @Bean
  public MeterRegistry basicRegistry() {
    return new LoggingMeterRegistry();
  }

  public static void main(String[] args) {
    SpringApplication.run(FelineApplication.class, args);
  }
}
