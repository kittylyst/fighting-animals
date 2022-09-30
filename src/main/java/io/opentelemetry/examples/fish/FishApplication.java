package io.opentelemetry.examples.fish;

import io.opentelemetry.examples.utils.OpenTelemetryConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FishApplication {

  public static void main(String[] args) {
    // Configure OpenTelemetry as early as possible
    OpenTelemetryConfig.configureGlobal("fish-app");
    SpringApplication.run(FishApplication.class, args);
  }
}