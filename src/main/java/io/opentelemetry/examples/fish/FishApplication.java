/* (C)2022-2023 */
package io.opentelemetry.examples.fish;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Metrics;
import java.util.Optional;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class FishApplication {

  @Bean
  @ConditionalOnClass(name = "io.opentelemetry.javaagent.OpenTelemetryAgent")
  public MeterRegistry otelRegistry() {
    Optional<MeterRegistry> otelRegistry =
        Metrics.globalRegistry.getRegistries().stream()
            .filter(r -> r.getClass().getName().contains("OpenTelemetryMeterRegistry"))
            .findAny();
    otelRegistry.ifPresent(Metrics.globalRegistry::remove);
    return otelRegistry.orElse(null);
  }

  public static void main(String[] args) {
    SpringApplication.run(FishApplication.class, args);
  }
}
