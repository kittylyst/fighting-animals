/* Copyright (C) Red Hat 2023-2024 */
package io.opentelemetry.examples.feline;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FelineController {

  private static Logger LOGGER = LoggerFactory.getLogger(FelineController.class);

  private final List<String> CATS = List.of("tabby", "jaguar", "leopard");

  @GetMapping("/getAnimal")
  public String makeBattle() throws InterruptedException {
    LOGGER.debug("Hit Feline service at: " + System.currentTimeMillis());
    // Random pause
    Thread.sleep((int) (20 * Math.random()));
    // Return random cat
    return CATS.get((int) (CATS.size() * Math.random()));
  }
}
