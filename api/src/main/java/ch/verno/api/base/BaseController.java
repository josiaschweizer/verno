package ch.verno.api.base;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.UUID;

public abstract class BaseController {

  protected <T> ResponseEntity<T> ok(T body) {
    return ResponseEntity.ok()
            .headers(defaultHeaders())
            .body(body);
  }

  protected <T> ResponseEntity<T> created(T body) {
    return ResponseEntity.status(201)
            .headers(defaultHeaders())
            .body(body);
  }

  protected HttpHeaders defaultHeaders() {
    HttpHeaders h = new HttpHeaders();
    h.add("X-API-Version", "v1");
    h.add("X-Request-Id", UUID.randomUUID().toString());
    h.add("X-Server-Time", Instant.now().toString());
    return h;
  }
}