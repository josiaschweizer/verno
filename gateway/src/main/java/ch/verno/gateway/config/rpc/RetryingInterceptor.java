package ch.verno.gateway.config.rpc;

import jakarta.annotation.Nonnull;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.net.ConnectException;

public class RetryingInterceptor implements ClientHttpRequestInterceptor {

  private final int maxAttempts;
  private final long delayMillis;

  public RetryingInterceptor(final int maxAttempts,
                             final long delayMillis) {
    this.maxAttempts = maxAttempts;
    this.delayMillis = delayMillis;
  }

  @Nonnull
  @Override
  public ClientHttpResponse intercept(@Nonnull final HttpRequest request,
                                      @Nonnull final byte[] body,
                                      @Nonnull final ClientHttpRequestExecution execution) throws IOException {
    IOException lastException = null;

    for (int attempt = 1; attempt <= maxAttempts; attempt++) {
      try {
        return execution.execute(request, body);
      } catch (ConnectException exception) {
        lastException = exception;
        if (attempt < maxAttempts) {
          try {
            Thread.sleep(delayMillis);
          } catch (InterruptedException interrupted) {
            Thread.currentThread().interrupt();
            throw new IOException("Retry interrupted", interrupted);
          }
        }
      }
    }
    throw lastException;
  }
}