/* Copyright (C) Red Hat 2024 */
package io.opentelemetry.examples.mammal;

import static io.opentelemetry.examples.utils.Misc.fetchAnimal;
import static io.opentelemetry.examples.utils.Misc.serverSpan;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.examples.utils.HttpServletRequestExtractor;
import io.opentelemetry.examples.utils.Misc;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MammalController {
  private static final Map<String, String> SERVICES =
      Map.of(
          "mustelids", "http://mustelid-service:8084/getAnimal",
          "felines", "http://feline-service:8085/getAnimal");

  private static final HttpServletRequestExtractor EXTRACTOR = new HttpServletRequestExtractor();

  private final HttpServletRequest httpServletRequest;

  private final OpenTelemetry sdk;
  private final Tracer tracer;

  public MammalController(HttpServletRequest httpServletRequest, OpenTelemetry sdk) {
    this.httpServletRequest = httpServletRequest;
    this.sdk = sdk;
    this.tracer = sdk.getTracer(MammalController.class.getName());
  }

  @GetMapping("/getAnimal")
  public String makeBattle() throws IOException, InterruptedException {
    // Extract the propagated context from the request. In this example, context will be
    // extracted from the Animal Service.
    var extractedContext = Misc.extractContext(httpServletRequest, EXTRACTOR);

    try (var scope = extractedContext.makeCurrent()) {
      // Start a span in the scope of the extracted context.
      var span = serverSpan(tracer, "/getAnimal", HttpMethod.GET.name(), "mammal-service:8081");

      // Send the sub-requests, return the response and end the span
      try {
        return fetchRandomAnimal(span);
      } finally {
        span.end();
      }
    }
  }

  private String fetchRandomAnimal(Span span) throws IOException, InterruptedException {
    List<String> keys = List.copyOf(SERVICES.keySet());
    var world = keys.get((int) (SERVICES.size() * Math.random()));
    var location = SERVICES.get(world);

    return fetchAnimal(span, world, location);
  }
}
