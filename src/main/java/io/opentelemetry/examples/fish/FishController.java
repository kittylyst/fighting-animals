/* Copyright (C) Red Hat 2024 */
package io.opentelemetry.examples.fish;

import static io.opentelemetry.examples.utils.Misc.extractContext;
import static io.opentelemetry.examples.utils.Misc.serverSpan;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.examples.utils.HttpServletRequestExtractor;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FishController {
  private static final HttpServletRequestExtractor EXTRACTOR = new HttpServletRequestExtractor();
  private static final List<String> FISH = List.of("salmon", "cod", "turbot");

  private final HttpServletRequest httpServletRequest;

  private final OpenTelemetry sdk;
  private final Tracer tracer;

  public FishController(HttpServletRequest httpServletRequest, OpenTelemetry sdk) {
    this.httpServletRequest = httpServletRequest;
    this.sdk = sdk;
    this.tracer = sdk.getTracer(FishController.class.getName());
  }

  @GetMapping("/getAnimal")
  public String makeBattle() throws IOException, InterruptedException {
    // Extract the propagated context from the request. In this example, context will be
    // extracted from the Animal Service.
    var extractedContext = extractContext(httpServletRequest, EXTRACTOR);

    try (var scope = extractedContext.makeCurrent()) {
      // Start a span in the scope of the extracted context.
      var span = serverSpan(tracer, "/getAnimal", HttpMethod.GET.name(), "fish-service:8083");

      try {
        // Random pause
        Thread.sleep((int) (20 * Math.random()));
        // Return random fish
        return FISH.get((int) (FISH.size() * Math.random()));
      } finally {
        span.end();
      }
    }
  }
}
