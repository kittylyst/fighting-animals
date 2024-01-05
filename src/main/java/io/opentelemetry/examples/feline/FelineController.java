/* Copyright (C) Red Hat 2024 */
package io.opentelemetry.examples.feline;

import static io.opentelemetry.examples.utils.Misc.extractContext;
import static io.opentelemetry.examples.utils.Misc.serverSpan;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.examples.utils.HttpServletRequestExtractor;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FelineController {
  private final List<String> CATS = List.of("tabby", "jaguar", "leopard");

  private static final HttpServletRequestExtractor EXTRACTOR = new HttpServletRequestExtractor();

  private final HttpServletRequest httpServletRequest;

  private final OpenTelemetry sdk;

  public FelineController(HttpServletRequest httpServletRequest, OpenTelemetry sdk) {
    this.httpServletRequest = httpServletRequest;
    this.sdk = sdk;
  }

  @GetMapping("/getAnimal")
  public String makeBattle() throws IOException, InterruptedException {
    // Extract the propagated context from the request. In this example, context will be
    // extracted from the Animal Service.
    var extractedContext = extractContext(httpServletRequest, EXTRACTOR);

    try (var scope = extractedContext.makeCurrent()) {
      // Start a span in the scope of the extracted context.
      var span =
          serverSpan(
              sdk,
              "/getAnimal",
              HttpMethod.GET.name(),
              FelineController.class.getName(),
              "feline-service:8085");

      try {
        // Random pause
        Thread.sleep((int) (20 * Math.random()));
        // Return random mammal
        return CATS.get((int) (CATS.size() * Math.random()));
      } finally {
        span.end();
      }
    }
  }
}
