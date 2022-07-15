package vn.vna.eri.test;

import static org.junit.Assert.assertEquals;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import org.junit.Test;

public class ServerConfigApiTest {

  public static final String TEMPLATE_URL_K = "http://localhost:8080/api/config?key=%s";
  public static final String TEMPLATE_URL_KV = "http://localhost:8080/api/config?key=%s&value=%s";
  public static HttpClient httpClient = HttpClient.newHttpClient();

  @Test
  public void apiConfigTest() {
    try {
      HttpRequest request = HttpRequest.newBuilder()
          .GET()
          .uri(new URI(String.format(TEMPLATE_URL_K, "CONFIG_TEST")))
          .build();

      httpClient.sendAsync(request, BodyHandlers.ofString())
          .thenApply(HttpResponse::body)
          .thenAccept((str) -> {
            assertEquals(str,
                "{\"success\":false,\"error\":\"Unable to find vn.vna.eri.v2.db.ServerConfigRepository$ServerConfig with id CONFIG_TEST\"}");
          });
    } catch (Exception ex) {
      assertEquals(0, 1);
    }
  }

}
