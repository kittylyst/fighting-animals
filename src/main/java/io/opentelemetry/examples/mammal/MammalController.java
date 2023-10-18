/* Copyright (C) Red Hat 2023 */
package io.opentelemetry.examples.mammal;

import static io.opentelemetry.examples.utils.Misc.fetchAnimal;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics;
import io.micrometer.core.instrument.binder.system.ProcessorMetrics;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
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

  private final MeterRegistry registry;

  public MammalController(MeterRegistry registry) {
    this.registry = registry;
    new ProcessorMetrics().bindTo(this.registry);
    new JvmMemoryMetrics().bindTo(this.registry);
  }

  @GetMapping("/getAnimal")
  public String makeBattle() throws IOException, InterruptedException {
    return fetchRandomAnimal();
  }

  private String fetchRandomAnimal() throws IOException, InterruptedException {
    List<String> keys = List.copyOf(SERVICES.keySet());
    var world = keys.get((int) (SERVICES.size() * Math.random()));
    var location = SERVICES.get(world);

    return fetchAnimal(world, location);
  }
}
