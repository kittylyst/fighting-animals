/* Copyright (C) Red Hat 2023-2024 */
package io.opentelemetry.examples.animal;

import ch.qos.logback.classic.LoggerContext;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.instrumentation.logback.appender.v1_0.OpenTelemetryAppender;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
public class AnimalApplication {

  @ConditionalOnClass(LoggerContext.class)
  @ConditionalOnProperty(name = "otel.instrumentation.logback.enabled", matchIfMissing = true)
  @Configuration
  static class LogbackAppenderConfig {

    @Bean
    ApplicationListener<ApplicationReadyEvent> logbackOtelAppenderInitializer(
        OpenTelemetry openTelemetry) {
      return event -> OpenTelemetryAppender.install(openTelemetry);
    }
  }

  public static void main(String[] args) {
    SpringApplication.run(AnimalApplication.class, args);
  }
}
