package io.opentelemetry.examples.utils;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.propagation.ContextPropagators;
import io.opentelemetry.exporter.otlp.metrics.OtlpGrpcMetricExporter;
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.metrics.SdkMeterProvider;
import io.opentelemetry.sdk.metrics.export.PeriodicMetricReader;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;
import io.opentelemetry.semconv.trace.attributes.SemanticAttributes;

import javax.servlet.http.HttpServletRequest;
import java.net.http.HttpRequest;
import java.time.Duration;
import java.util.UUID;
import java.util.function.Supplier;

import static io.opentelemetry.semconv.resource.attributes.ResourceAttributes.SERVICE_INSTANCE_ID;
import static io.opentelemetry.semconv.resource.attributes.ResourceAttributes.SERVICE_NAME;

public class OpenTelemetryConfig {

  private static final Supplier<String> OTLP_HOST_SUPPLIER = () -> {
    var defaultUrl = "http://otel-collector:4317";
    var envUrl = System.getenv("OTLP_HOST"); // Maek URL not HOST
    if (envUrl == null || !envUrl.equals("")) {
      return defaultUrl;
    }
    return envUrl;
  };

  public static void configureGlobal(String defaultServiceName) {
    var resource = configureResource(defaultServiceName);

    // Configure traces
    var spanExporterBuilder =
        OtlpGrpcSpanExporter.builder()
            .setEndpoint(OTLP_HOST_SUPPLIER.get());
//            .addHeader("api-key", "");

    // Enable retry policy via unstable API
//    DefaultGrpcExporterBuilder.getDelegateBuilder(
//            OtlpGrpcSpanExporterBuilder.class, spanExporterBuilder)
//        .addRetryPolicy(RetryPolicy.getDefault());

    var sdkTracerProviderBuilder =
        SdkTracerProvider.builder()
            .setResource(resource)
            .addSpanProcessor(BatchSpanProcessor.builder(spanExporterBuilder.build()).build());
    OpenTelemetrySdk.builder()
        .setPropagators(ContextPropagators.create(W3CTraceContextPropagator.getInstance()))
        .setTracerProvider(sdkTracerProviderBuilder.build())
        .buildAndRegisterGlobal();

    // Configure metrics
    var meterProviderBuilder = SdkMeterProvider.builder().setResource(resource);

    var metricExporterBuilder =
        OtlpGrpcMetricExporter.builder()
//            .setPreferredTemporality(AggregationTemporality.DELTA)
            .setEndpoint(OTLP_HOST_SUPPLIER.get());

    // Enable retry policy via unstable API
//    DefaultGrpcExporterBuilder.getDelegateBuilder(
//            OtlpGrpcMetricExporterBuilder.class, metricExporterBuilder)
//        .addRetryPolicy(RetryPolicy.getDefault());

    meterProviderBuilder.registerMetricReader(PeriodicMetricReader.builder(metricExporterBuilder.build())
            .setInterval(Duration.ofSeconds(5))
            .build()).build();

    // How do we register this in global context
//    GlobalMeterProvider.set(meterProviderBuilder.build());
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

  public static Context extractContext(HttpServletRequest httpServletRequest , HttpServletRequestExtractor extractor) {
    return GlobalOpenTelemetry.getPropagators()
            .getTextMapPropagator()
            .extract(Context.current(), httpServletRequest, extractor);
  }

  private static Resource configureResource(String serviceName) {
    return Resource.getDefault()
        .merge(
            Resource.builder()
                .put(
                    SERVICE_NAME,
                    serviceName)
                .put(SERVICE_INSTANCE_ID, UUID.randomUUID().toString())
                .build());
  }

  /**
   * Create a {@link SpanKind#SERVER} span, setting the parent context if available
   *
   * @param path the HTTP path
   * @param method the HTTP method
   * @return the span
   */
  public static Span serverSpan(String path, String method, String tracerName, String serviceName) {
    return GlobalOpenTelemetry.getTracer(tracerName)
            .spanBuilder(path)
            .setSpanKind(SpanKind.SERVER)
            .setAttribute(SemanticAttributes.HTTP_METHOD, method)
            .setAttribute(SemanticAttributes.HTTP_SCHEME, "http")
            .setAttribute(SemanticAttributes.HTTP_HOST, serviceName)
            .setAttribute(SemanticAttributes.HTTP_TARGET, path)
            .startSpan();
  }

  private OpenTelemetryConfig() {}
}

