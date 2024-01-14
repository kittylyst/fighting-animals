/* Copyright (C) Red Hat 2023-2024 */
package io.opentelemetry.examples.animal;

import static io.opentelemetry.examples.utils.Misc.fetchAnimal;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics;
import io.micrometer.core.instrument.binder.system.ProcessorMetrics;
import io.micrometer.core.instrument.config.MeterFilter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AnimalController {
  private static final Map<String, String> SERVICES =
      Map.of(
          "mammals", "http://mammal-service:8081/getAnimal",
          "fish", "http://fish-service:8083/getAnimal");

  private final Counter battlesTotal;

  private final Timer responseTimer;

  private final DistributionSummary winSummary;

  private final MeterRegistry registry;

  public AnimalController(MeterRegistry registry) {
    this.registry = registry;
    this.battlesTotal = this.registry.counter("battles.total");
    this.responseTimer =
        Timer.builder("response.time").description("Response time").register(registry);

    // Summarizes the size of the attacker's strength when it wins
    this.winSummary = registry.summary("attacker.win.size");

    // This next line prevents the internal metrics from being published
    // We don't actually have any internal metrics, but it's a nice example
    this.registry.config().meterFilter(MeterFilter.denyNameStartsWith("internal"));

    // The above filter is equivalent to the following construction
    //    MeterFilter f = new MeterFilter() {
    //      @Override
    //      public MeterFilterReply accept(Meter.Id id) {
    //        if(id.getName().startsWith("internal")) {
    //          return MeterFilterReply.DENY;
    //        }
    //        return MeterFilterReply.NEUTRAL;
    //      }
    //    };

    // These next two lines switch on CPU & memory metrics for delivery through Micrometer
    new ProcessorMetrics().bindTo(this.registry);
    new JvmMemoryMetrics().bindTo(this.registry);
  }

  @GetMapping("/battle")
  public String makeBattle() throws Exception {
    Callable<String> callable =
        () -> {
          // Send the two requests and return the response body as the response
          var good = fetchRandomAnimal();
          var evil = fetchRandomAnimal();
          return "{ \"good\": \"" + good + "\", \"evil\": \"" + evil + "\" }";
        };
    battlesTotal.increment();
    return responseTimer.recordCallable(callable);
  }

  @GetMapping("/fight")
  public String resolveFight(@RequestParam String attacker, @RequestParam String defender) {
    final String winner;
    // Defenders strength is taken to be 0.5
    var attackerStrength = Math.random();
    if (attackerStrength > 0.5) {
      winner = attacker;
      // Add to the distribution summary
      winSummary.record(attackerStrength);
    } else {
      winner = defender;
    }

    return "{ \"winner\": " + winner + " }";
  }

  private String fetchRandomAnimal() throws IOException, InterruptedException {
    List<String> keys = List.copyOf(SERVICES.keySet());
    var world = keys.get((int) (SERVICES.size() * Math.random()));
    var location = SERVICES.get(world);

    return fetchAnimal(world, location);
  }
}
