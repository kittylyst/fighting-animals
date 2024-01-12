/* Copyright (C) Red Hat 2023-2024 */
package io.opentelemetry.examples.feline;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FelineController {
  private final List<String> CATS = List.of("tabby", "jaguar", "leopard");

  @GetMapping("/getAnimal")
  public String makeBattle() throws InterruptedException {
    // Random pause
    Thread.sleep((int) (20 * Math.random()));
    // Return random cat
    return CATS.get((int) (CATS.size() * Math.random()));
  }
}
