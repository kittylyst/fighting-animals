/* Copyright (C) Red Hat 2023 */
package io.opentelemetry.examples.feline;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics;
import io.micrometer.core.instrument.binder.system.ProcessorMetrics;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FelineController {
  private final List<String> CATS = List.of("tabby", "jaguar", "leopard");

  @Autowired private HttpServletRequest httpServletRequest;

  private final MeterRegistry registry;
  private final Counter numCombatants;

  public FelineController(MeterRegistry registry) {
    this.registry = registry;
    this.numCombatants =
        Counter.builder("battles.combatants").tag("type", "feline").register(this.registry);
    new ProcessorMetrics().bindTo(this.registry);
    new JvmMemoryMetrics().bindTo(this.registry);
  }

  @GetMapping("/getAnimal")
  public String makeBattle() throws InterruptedException {
    // Random pause
    Thread.sleep((int) (20 * Math.random()));
    numCombatants.increment();
    // Return random cat
    return CATS.get((int) (CATS.size() * Math.random()));
  }
}
