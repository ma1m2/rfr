package io.student.rangiffler.jupiter.extension;

import io.student.rangiffler.config.TestConfig;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public final class StandReadinessExtension implements SuiteExtension {
  private static final Duration REQUEST = Duration.ofSeconds(2);

  @Override
  public void beforeAll(ExtensionContext context) {
    if (!TestConfig.standCheck()) {
      return;
    }
    if (!context.getTags().contains("ui")) {
      return;
    }
    SuiteExtension.super.beforeAll(context);
  }

  @Override
  public void beforeSuite(ExtensionContext context) {
    new Stand(Duration.ofSeconds(TestConfig.standTimeout())).await();
  }

  private static final class Stand {
    private final Duration limit;

    private Stand(Duration limit) {
      this.limit = limit;
    }

    private void await() {
      long deadline = System.nanoTime() + this.limit.toNanos();
      try (HttpClient client = HttpClient.newBuilder().connectTimeout(REQUEST).build()) {
        while (true) {
          List<String> down = this.down(client);
          if (down.isEmpty()) {
            return;
          }
          long left = deadline - System.nanoTime();
          if (left <= 0) {
            throw new IllegalStateException(
                "Stand was not ready in " + this.limit.toSeconds() + "s: " + String.join(", ", down)
            );
          }
          TimeUnit.NANOSECONDS.sleep(Math.min(TimeUnit.MILLISECONDS.toNanos(500), left));
        }
      } catch (InterruptedException exception) {
        Thread.currentThread().interrupt();
        throw new IllegalStateException("Stand wait was interrupted", exception);
      }
    }

    private List<String> down(HttpClient client) {
      List<String> down = new ArrayList<>();
      if (!this.mysql()) {
        down.add("mysql; start it with docker compose up -d --wait");
      }
      if (!this.http(client, TestConfig.authUrl())) {
        down.add("rfr-auth; start it with ./gradlew runAll");
      }
      if (!this.http(client, TestConfig.apiUrl())) {
        down.add("rfr-api; start it with ./gradlew runAll");
      }
      if (!this.http(client, TestConfig.frontUrl())) {
        down.add("rfr-gql-client; start it with npm run dev");
      }
      return down;
    }

    private boolean mysql() {
      String raw = TestConfig.dbUrl("rangiffler-auth");
      int scheme = raw.indexOf("://");
      String rest = scheme < 0 ? raw : raw.substring(scheme + 3);
      int slash = rest.indexOf('/');
      String hostPort = slash < 0 ? rest : rest.substring(0, slash);
      int colon = hostPort.lastIndexOf(':');
      String host = colon < 0 ? hostPort : hostPort.substring(0, colon);
      int port = colon < 0 ? 3306 : Integer.parseInt(hostPort.substring(colon + 1));
      try (Socket socket = new Socket()) {
        socket.connect(new InetSocketAddress(host, port), (int) REQUEST.toMillis());
        return true;
      } catch (IOException exception) {
        return false;
      }
    }

    private boolean http(HttpClient client, String url) {
      try {
        client.send(
            HttpRequest.newBuilder(URI.create(url)).timeout(REQUEST).GET().build(),
            HttpResponse.BodyHandlers.discarding()
        );
        return true;
      } catch (IOException | InterruptedException exception) {
        if (exception instanceof InterruptedException) {
          Thread.currentThread().interrupt();
        }
        return false;
      }
    }
  }
}
