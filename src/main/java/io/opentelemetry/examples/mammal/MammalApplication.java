/* Copyright (C) Red Hat 2023 */
package io.opentelemetry.examples.mammal;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.logging.LoggingMeterRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MammalApplication {

  @Bean
  public MeterRegistry basicRegistry() {
    return new LoggingMeterRegistry();
  }

  public static void main(String[] args) {
    SpringApplication.run(MammalApplication.class, args);
  }
}
