/* Copyright (C) Red Hat 2023-2024 */
package io.opentelemetry.examples.animal;

import static io.opentelemetry.examples.utils.Misc.fetchAnimal;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.metrics.LongCounter;
import io.opentelemetry.api.metrics.Meter;
import io.opentelemetry.api.metrics.ObservableDoubleGauge;
import io.opentelemetry.sdk.autoconfigure.AutoConfiguredOpenTelemetrySdk;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AnimalController {
  private static final Map<String, String> SERVICES =
      Map.of(
          "mammals", "http://mammal-service:8081/getAnimal",
          "fish", "http://fish-service:8083/getAnimal");

  public static final String INSTRUMENTATION_SCOPE = "io.opentelemetry.examples.animal";

  private final Meter appMeter;
  private final Meter memoryMeter;
  private final LongCounter battlesTotal;
  private final ObservableDoubleGauge cpuTotal;

  public AnimalController() {
    OpenTelemetry sdk = AutoConfiguredOpenTelemetrySdk.initialize().getOpenTelemetrySdk();

    Meter appMeter = sdk.getMeter(INSTRUMENTATION_SCOPE + ".app");
    this.appMeter = appMeter;
    this.battlesTotal = createCounter(appMeter);

    Meter memoryMeter = sdk.getMeter(INSTRUMENTATION_SCOPE + ".memory");
    this.memoryMeter = memoryMeter;
    this.cpuTotal = createGauge(memoryMeter);
  }

  static LongCounter createCounter(Meter meter) {
    return meter
        .counterBuilder("battles.total")
        .setDescription("Counts total battles fought.")
        .setUnit("unit")
        .build();
  }

  static ObservableDoubleGauge createGauge(Meter meter) {
    return meter
        .gaugeBuilder("jvm.memory.total")
        .setDescription("Reports JVM memory usage.")
        .setUnit("byte")
        .buildWithCallback(
            result -> result.record(Runtime.getRuntime().totalMemory(), Attributes.empty()));
  }

  @GetMapping("/battle")
  public String makeBattle() throws Exception {
    // Send the two requests and return the response body as the response
    var good = fetchRandomAnimal();
    var evil = fetchRandomAnimal();
    battlesTotal.add(1);
    return "{ \"good\": \"" + good + "\", \"evil\": \"" + evil + "\" }";
  }

  private String fetchRandomAnimal() throws IOException, InterruptedException {
    List<String> keys = List.copyOf(SERVICES.keySet());
    var world = keys.get((int) (SERVICES.size() * Math.random()));
    var location = SERVICES.get(world);

    return fetchAnimal(world, location);
  }
}
