/* Copyright (C) Red Hat 2023-2024 */
package io.opentelemetry.examples.mammal;

import static io.opentelemetry.examples.utils.Misc.fetchAnimal;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics;
import io.micrometer.core.instrument.binder.system.ProcessorMetrics;
import io.opentelemetry.examples.utils.FelinePercent;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MammalController {
  private static final Map<String, String> SERVICES =
      Map.of(
          "mustelids", "http://mustelid-service:8084/getAnimal",
          "felines", "http://feline-service:8085/getAnimal");

  @Autowired private HttpServletRequest httpServletRequest;

  private final FelinePercent felinePercent;
  private int felineCount = 0;
  private int mustelidCount = 0;

  private final MeterRegistry registry;

  public MammalController(MeterRegistry registry) {
    this.registry = registry;
    felinePercent = this.registry.gauge("battles.felinePercent", new FelinePercent(0.5));

    new ProcessorMetrics().bindTo(this.registry);
    new JvmMemoryMetrics().bindTo(this.registry);
  }

  @GetMapping("/getAnimal")
  public String getAnimal() throws IOException, InterruptedException {
    List<String> keys = List.copyOf(SERVICES.keySet());
    var id = (int) (SERVICES.size() * Math.random());
    if (id == 0) {
      mustelidCount += 1;
    } else {
      felineCount += 1;
    }
    felinePercent.setValue((double) felineCount / (double) (felineCount + mustelidCount));

    var world = keys.get(id);
    var location = SERVICES.get(world);

    return fetchAnimal(world, location);
  }
}
