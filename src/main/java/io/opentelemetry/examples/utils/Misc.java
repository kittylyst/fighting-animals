package io.opentelemetry.examples.utils;

import io.opentelemetry.api.trace.Span;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static io.opentelemetry.examples.utils.OpenTelemetryConfig.injectContext;

public class Misc {

    public static String fetchAnimal(Span span, String world, String targetUri) throws IOException, InterruptedException {
        var location = URI.create(targetUri);

        var client = HttpClient.newHttpClient();
        var requestBuilder = HttpRequest.newBuilder().uri(location);

        // Inject the span's content into the request's headers.
        injectContext(span, requestBuilder);
        var request = requestBuilder.build();

        return client.send(request, HttpResponse.BodyHandlers.ofString()).body();
    }


    private Misc() {}
}
