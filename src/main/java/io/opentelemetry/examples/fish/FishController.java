/* (C)2022-2023 */
package io.opentelemetry.examples.fish;

import io.micrometer.core.instrument.MeterRegistry;
import io.opentelemetry.examples.utils.HttpServletRequestExtractor;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FishController {
  private static final HttpServletRequestExtractor EXTRACTOR = new HttpServletRequestExtractor();
  private static final List<String> FISH = List.of("salmon", "cod", "turbot");

  @Autowired private HttpServletRequest httpServletRequest;

  private final MeterRegistry registry;

  public FishController(MeterRegistry registry) {
    this.registry = registry;
  }

  @GetMapping("/getAnimal")
  public String makeBattle() throws IOException, InterruptedException {
    // Random pause
    Thread.sleep((int) (20 * Math.random()));
    // Return random fish
    return FISH.get((int) (FISH.size() * Math.random()));
  }
}
