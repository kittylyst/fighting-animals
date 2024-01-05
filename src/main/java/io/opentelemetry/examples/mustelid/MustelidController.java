/* Copyright (C) Red Hat 2023-2024 */
package io.opentelemetry.examples.mustelid;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MustelidController {
  private final List<String> MUSTELIDS = List.of("otter", "badger", "marten", "weasel");

  @GetMapping("/getAnimal")
  public String getAnimal() throws InterruptedException {
    // Random pause
    Thread.sleep((int) (20 * Math.random()));

    // Return random mustelid
    return MUSTELIDS.get((int) (MUSTELIDS.size() * Math.random()));
  }
}
