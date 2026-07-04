package ch.verno.gateway.base;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.UUID;

public abstract class BaseController {

  @Nonnull
  protected <T> ResponseEntity<T> ok(@Nullable final T body) {
    return ResponseEntity.ok()
            .headers(defaultHeaders())
            .body(body);
  }

  @Nonnull
  protected <T> ResponseEntity<T> created(@Nullable final T body) {
    return ResponseEntity.status(HttpStatus.CREATED)
            .headers(defaultHeaders())
            .body(body);
  }

  @Nonnull
  protected <T> ResponseEntity<T> failedCreating(@Nullable final T body) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .headers(defaultHeaders())
            .body(body);
  }

  protected HttpHeaders defaultHeaders() {
    final var header = new HttpHeaders();
    header.add("X-API-Version", "v1");
    header.add("X-Request-Id", UUID.randomUUID().toString());
    header.add("X-Server-Time", Instant.now().toString());
    return header;
  }
}