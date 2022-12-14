package io.opentelemetry.examples.mammal;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.propagation.TextMapGetter;
import io.opentelemetry.examples.utils.HttpServletRequestExtractor;
import io.opentelemetry.semconv.trace.attributes.SemanticAttributes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

import static io.opentelemetry.examples.utils.Misc.fetchAnimal;
import static io.opentelemetry.examples.utils.OpenTelemetryConfig.*;

@RestController
public class MammalController {
    private static final Map<String, String> SERVICES = Map.of(
            "mustelids", "http://mustelid-service:8084/getAnimal",
            "felines", "http://feline-service:8085/getAnimal");

    private static final HttpServletRequestExtractor EXTRACTOR = new HttpServletRequestExtractor();

    @Autowired private HttpServletRequest httpServletRequest;

    @GetMapping("/getAnimal")
    public String makeBattle() throws IOException, InterruptedException {
        // Extract the propagated context from the request. In this example, context will be
        // extracted from the Animal Service.
        var extractedContext = extractContext(httpServletRequest,EXTRACTOR);

        try (var scope = extractedContext.makeCurrent()) {
            // Start a span in the scope of the extracted context.
            var span = serverSpan("/getAnimal", HttpMethod.GET.name(), MammalController.class.getName(), "mammal-service:8081");

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
