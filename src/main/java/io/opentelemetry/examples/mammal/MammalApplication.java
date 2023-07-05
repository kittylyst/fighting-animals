/* (C)2023 */
package io.opentelemetry.examples.mammal;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Metrics;
import java.util.Optional;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MammalApplication {

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
    SpringApplication.run(MammalApplication.class, args);
  }
}
