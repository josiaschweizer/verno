package ch.verno.provisioner.api.exception;

import jakarta.annotation.Nonnull;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(IllegalArgumentException.class)
  public ErrorResponse notFound(final IllegalArgumentException ex) {
    return new ErrorResponse("not_found", ex.getMessage());
  }

  @ResponseStatus(HttpStatus.CONFLICT)
  @ExceptionHandler(DuplicateKeyException.class)
  public ErrorResponse conflict(@Nonnull final DuplicateKeyException ex) {
    return new ErrorResponse("conflict", "resource already exists");
  }

  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler(IllegalStateException.class)
  public ErrorResponse illegalState(@Nonnull final IllegalStateException ex) {
    return new ErrorResponse("internal_error", ex.getMessage());
  }

  public record ErrorResponse(String code, String message) {
  }
}