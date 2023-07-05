/* (C)2022-2023 */
package io.opentelemetry.examples.utils;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Misc {

  public static String fetchAnimal(String targetUri) throws IOException, InterruptedException {
    var location = URI.create(targetUri);

    var client = HttpClient.newHttpClient();
    var requestBuilder = HttpRequest.newBuilder().uri(location);

    var request = requestBuilder.build();

    return client.send(request, HttpResponse.BodyHandlers.ofString()).body();
  }

  private Misc() {}
}
