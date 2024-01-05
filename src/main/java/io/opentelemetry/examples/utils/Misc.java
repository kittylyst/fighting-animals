/* Copyright (C) Red Hat 2024 */
package io.opentelemetry.examples.utils;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.context.Context;
import io.opentelemetry.semconv.SemanticAttributes;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Misc {
  private Misc() {}

  public static String fetchAnimal(Span span, String world, String targetUri)
      throws IOException, InterruptedException {
    var location = URI.create(targetUri);

    var client = HttpClient.newHttpClient();
    var requestBuilder = HttpRequest.newBuilder().uri(location);

    // Inject the span's content into the request's headers.
    injectContext(span, requestBuilder);
    var request = requestBuilder.build();

    return client.send(request, HttpResponse.BodyHandlers.ofString()).body();
  }

  /**
   * Create a {@link SpanKind#SERVER} span, setting the parent context if available
   *
   * @param sdk
   * @param path the HTTP path
   * @param method the HTTP method
   * @return the span
   */
  public static Span serverSpan(
      OpenTelemetry sdk, String path, String method, String tracerName, String serviceName) {
    return sdk.getTracer(tracerName)
        .spanBuilder(path)
        .setSpanKind(SpanKind.SERVER)
        .setAttribute(SemanticAttributes.HTTP_METHOD, method)
        .setAttribute(SemanticAttributes.HTTP_SCHEME, "http")
        .setAttribute(SemanticAttributes.HTTP_HOST, serviceName)
        .setAttribute(SemanticAttributes.HTTP_TARGET, path)
        .startSpan();
  }

  /**
   * Inject the {@code span}'s context into the {@code requestBuilder}.
   *
   * @param span the span
   * @param requestBuilder the request builder
   */
  public static void injectContext(Span span, HttpRequest.Builder requestBuilder) {
    var context = Context.current().with(span);
    GlobalOpenTelemetry.getPropagators()
        .getTextMapPropagator()
        .inject(context, requestBuilder, HttpRequest.Builder::header);
  }

  public static Context extractContext(
      HttpServletRequest httpServletRequest, HttpServletRequestExtractor extractor) {
    return GlobalOpenTelemetry.getPropagators()
        .getTextMapPropagator()
        .extract(Context.current(), httpServletRequest, extractor);
  }
}
