/* Copyright (C) Red Hat 2024 */
package io.opentelemetry.examples.animal;

import static io.opentelemetry.examples.utils.Misc.fetchAnimal;
import static io.opentelemetry.examples.utils.Misc.serverSpan;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.examples.utils.HttpServletRequestExtractor;
import io.opentelemetry.examples.utils.Misc;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AnimalController {
  private static final Map<String, String> SERVICES =
      Map.of(
          "mammals", "http://mammal-service:8081/getAnimal",
          "fish", "http://fish-service:8083/getAnimal");

  private static final HttpServletRequestExtractor EXTRACTOR = new HttpServletRequestExtractor();

  @Autowired private HttpServletRequest httpServletRequest;

  @GetMapping("/battle")
  public String makeBattle() throws IOException, InterruptedException {
    // Extract the propagated context from the request. In this example, no context will be
    // extracted from the request since this route initializes the trace.
    var extractedContext = Misc.extractContext(httpServletRequest, EXTRACTOR);

    try (var scope = extractedContext.makeCurrent()) {
      // Start a span in the scope of the extracted context.
      var span =
          serverSpan(
              "/battle",
              HttpMethod.GET.name(),
              AnimalController.class.getName(),
              "animal-service:8080");

      // Send the two requests and return the response body as the response, and end the span.
      try {
        var good = fetchRandomAnimal(span);
        var evil = fetchRandomAnimal(span);
        return "{ \"good\": \"" + good + "\", \"evil\": \"" + evil + "\" }";
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
