/* Copyright (C) Red Hat 2023-2024 */
package io.opentelemetry.examples.mammal;

import static io.opentelemetry.examples.utils.Misc.fetchAnimal;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MammalController {
  private static final Map<String, String> SERVICES =
      Map.of(
          "mustelids", "http://mustelid-service:8084/getAnimal",
          "felines", "http://feline-service:8085/getAnimal");

  @GetMapping("/getAnimal")
  public String getAnimal() throws IOException, InterruptedException {
    List<String> keys = List.copyOf(SERVICES.keySet());
    var id = (int) (SERVICES.size() * Math.random());

    var world = keys.get(id);
    var location = SERVICES.get(world);

    return fetchAnimal(world, location);
  }
}
