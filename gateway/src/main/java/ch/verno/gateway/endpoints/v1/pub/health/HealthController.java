package ch.verno.gateway.endpoints.v1.pub.health;

import ch.verno.common.lib.api.ApiUrl;
import jakarta.annotation.Nonnull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiUrl.HEALTH)
public class HealthController {

  @Nonnull
  @GetMapping
  public ResponseEntity<String> health() {
    return ResponseEntity.ok("OK");
  }
  
}
