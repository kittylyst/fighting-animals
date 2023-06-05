/* Copyright (C) Red Hat 2023 */
package io.opentelemetry.examples.fish;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FishController {
  private static final List<String> FISH = List.of("salmon", "cod", "turbot");

  @Autowired private HttpServletRequest httpServletRequest;

  @GetMapping("/getAnimal")
  public String makeBattle() throws InterruptedException {
    // Random pause
    Thread.sleep((int) (20 * Math.random()));
    // Return random fish
    return FISH.get((int) (FISH.size() * Math.random()));
  }
}
