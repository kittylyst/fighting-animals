/* Copyright (C) Red Hat 2023-2024 */
package io.opentelemetry.examples.mustelid;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics;
import io.micrometer.core.instrument.binder.system.ProcessorMetrics;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MustelidController {
  private final List<String> MUSTELIDS = List.of("otter", "badger", "marten", "weasel");
  private final Counter numCombatants;

  @Autowired private HttpServletRequest httpServletRequest;

  private final MeterRegistry registry;

  public MustelidController(MeterRegistry registry) {
    this.registry = registry;
    this.numCombatants =
        Counter.builder("battles.combatants").tag("type", "mustelid").register(this.registry);

    new ProcessorMetrics().bindTo(this.registry);
    new JvmMemoryMetrics().bindTo(this.registry);
  }

  @GetMapping("/getAnimal")
  public String getAnimal() throws InterruptedException {
    // Random pause
    Thread.sleep((int) (20 * Math.random()));
    numCombatants.increment();

    // Return random mustelid
    return MUSTELIDS.get((int) (MUSTELIDS.size() * Math.random()));
  }
}
