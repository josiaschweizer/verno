package ch.verno.api;

import ch.verno.common.exceptions.server.service.TenantAlreadyExistsException;
import jakarta.annotation.Nonnull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

  @ExceptionHandler(TenantAlreadyExistsException.class)
  public ResponseEntity<?> handleTenantExists(@Nonnull final TenantAlreadyExistsException ex) {
    return ResponseEntity.status(409).body(Map.of(
            "status", 409,
            "message", ex.getMessage()
    ));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<?> handleValidation(@Nonnull final MethodArgumentNotValidException ex) {
    return ResponseEntity.status(400).body(Map.of(
            "status", 400,
            "message", "Validation failed",
            "details", ex.getBindingResult().getFieldErrors().stream()
                    .map(e -> e.getField() + ": " + e.getDefaultMessage())
                    .toList()
    ));
  }
}