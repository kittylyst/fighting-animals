package io.opentelemetry.examples.animal;

import io.opentelemetry.examples.utils.OpenTelemetryConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AnimalApplication {

  public static void main(String[] args) {
    // Configure OpenTelemetry as early as possible
    OpenTelemetryConfig.configureGlobal("animal-app");
    SpringApplication.run(AnimalApplication.class, args);
  }
}
