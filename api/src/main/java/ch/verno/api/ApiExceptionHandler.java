package ch.verno.api;

import ch.verno.common.exceptions.server.service.TenantAlreadyExistsException;
import jakarta.annotation.Nonnull;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

  @Nonnull
  @ExceptionHandler(TenantAlreadyExistsException.class)
  public ResponseEntity<?> handleTenantExists(@Nonnull final TenantAlreadyExistsException ex) {
    return ResponseEntity.status(409).body(Map.of(
            "status", 409,
            "code", "TENANT_ALREADY_EXISTS",
            "message", ex.getMessage()
    ));
  }

  @Nonnull
  @ExceptionHandler(IllegalStateException.class)
  public ResponseEntity<?> handleIllegalState(@Nonnull final IllegalStateException ex) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
            "status", HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "code", "TENANT_PROVISION_FAILED",
            "message", ex.getMessage()
    ));
  }

  @Nonnull
  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<?> handleDataIntegrity(@Nonnull final DataIntegrityViolationException ex) {
    ex.getMostSpecificCause();
    return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
            "status", HttpStatus.CONFLICT.value(),
            "code", "DATA_INTEGRITY_VIOLATION",
            "message", "Database constraint violated",
            "details", ex.getMostSpecificCause().getMessage()
    ));
  }

  @Nonnull
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<?> handleValidation(@Nonnull final MethodArgumentNotValidException ex) {
    return ResponseEntity.status(400).body(Map.of(
            "status", 400,
            "code", "VALIDATION_FAILED",
            "message", "Validation failed",
            "details", ex.getBindingResult().getFieldErrors().stream()
                    .map(e -> e.getField() + ": " + e.getDefaultMessage())
                    .toList()
    ));
  }
}