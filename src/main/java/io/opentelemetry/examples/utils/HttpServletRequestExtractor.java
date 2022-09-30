package io.opentelemetry.examples.utils;

import io.opentelemetry.context.propagation.TextMapGetter;

import javax.servlet.http.HttpServletRequest;

/**
 * A simple {@link TextMapGetter} implementation that extracts context from {@link
 * HttpServletRequest} headers.
 */
public class HttpServletRequestExtractor implements TextMapGetter<HttpServletRequest> {
    @Override
    public Iterable<String> keys(HttpServletRequest carrier) {
        return () -> carrier.getHeaderNames().asIterator();
    }

    @Override
    public String get(HttpServletRequest carrier, String key) {
        return carrier.getHeader(key);
    }
}
