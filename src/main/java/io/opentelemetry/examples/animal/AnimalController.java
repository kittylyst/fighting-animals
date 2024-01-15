/* Copyright (C) Red Hat 2023-2024 */
package io.opentelemetry.examples.animal;

import static io.opentelemetry.examples.utils.Misc.fetchAnimal;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AnimalController {
  private static final Logger LOGGER = LoggerFactory.getLogger(AnimalController.class);

  private static final Map<String, String> SERVICES =
      Map.of(
          "mammals", "http://mammal-service:8081/getAnimal",
          "fish", "http://fish-service:8083/getAnimal");

  @GetMapping("/battle")
  public String makeBattle() throws IOException, InterruptedException {
    // Send the two requests and return the response body as the response, and end the span.
    var good = fetchRandomAnimal();
    var evil = fetchRandomAnimal();
    return "{ \"good\": \"" + good + "\", \"evil\": \"" + evil + "\" }";
  }

  private String fetchRandomAnimal() throws IOException, InterruptedException {
    var pause = (int) (SERVICES.size() * Math.random());
    LOGGER.info("Pausing for: " + pause);
    List<String> keys = List.copyOf(SERVICES.keySet());
    var world = keys.get(pause);
    var location = SERVICES.get(world);

    return fetchAnimal(world, location);
  }
}
