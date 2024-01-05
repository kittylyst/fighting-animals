/* Copyright (C) Red Hat 2023-2024 */
package io.opentelemetry.examples.animal;

import static io.opentelemetry.examples.utils.Misc.fetchAnimal;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.metrics.LongCounter;
import io.opentelemetry.api.metrics.Meter;
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

  public static final String INSTRUMENTATION_SCOPE = "io.opentelemetry.example.metrics";

  private final Meter meter;
  private final LongCounter battlesTotal;

  public AnimalController() {
    OpenTelemetry sdk = AutoConfiguredOpenTelemetrySdk.initialize().getOpenTelemetrySdk();
    Meter meter = sdk.getMeter(INSTRUMENTATION_SCOPE);
    this.meter = meter;
    battlesTotal = createCounter(meter);
    //    this.responseTimer =
    //        Timer.builder("response.time").description("Response time").register(registry);
  }

  static LongCounter createCounter(Meter meter) {
    return meter
        .counterBuilder("battles.total")
        .setDescription("Counts total battles fought.")
        .setUnit("unit")
        .build();
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
