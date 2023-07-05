/* (C)2022-2023 */
package io.opentelemetry.examples.mustelid;

import io.opentelemetry.examples.utils.HttpServletRequestExtractor;
import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MustelidController {
  private final List<String> MUSTELIDS = List.of("otter", "badger", "marten", "weasel");

  private static final HttpServletRequestExtractor EXTRACTOR = new HttpServletRequestExtractor();

  @Autowired private HttpServletRequest httpServletRequest;

  @GetMapping("/getAnimal")
  public String makeBattle() throws IOException, InterruptedException {
    // Random pause
    Thread.sleep((int) (20 * Math.random()));
    // Return random mammal
    return MUSTELIDS.get((int) (MUSTELIDS.size() * Math.random()));
  }
}
