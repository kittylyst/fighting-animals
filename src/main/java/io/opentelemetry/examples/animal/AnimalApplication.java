/* Copyright (C) Red Hat 2023-2024 */
package io.opentelemetry.examples.animal;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.sdk.autoconfigure.AutoConfiguredOpenTelemetrySdk;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AnimalApplication {

  @Bean
  public OpenTelemetry openTelemetry() {
    return AutoConfiguredOpenTelemetrySdk.initialize().getOpenTelemetrySdk();
  }

  public static void main(String[] args) {
    SpringApplication.run(AnimalApplication.class, args);
  }
}
