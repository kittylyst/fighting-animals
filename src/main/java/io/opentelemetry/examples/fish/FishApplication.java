/* Copyright (C) Red Hat 2023 */
package io.opentelemetry.examples.fish;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.logging.LoggingMeterRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class FishApplication {
  @Bean
  public MeterRegistry basicRegistry() {
    return new LoggingMeterRegistry();
  }

  public static void main(String[] args) {
    SpringApplication.run(FishApplication.class, args);
  }
}
