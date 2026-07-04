package ch.verno.gateway.error;

import ch.verno.common.exceptions.db.DBNotFoundReason;
import ch.verno.contract.api.util.ApiErrorResponse;
import ch.verno.contract.rpc.RpcException;
import ch.verno.contract.rpc.RpcResponse;
import jakarta.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Optional;

@RestControllerAdvice
public class GatewayExceptionHandler {

  @Nonnull private static final Logger LOGGER = LoggerFactory.getLogger(GatewayExceptionHandler.class);

  @Nonnull private static final String INTERNAL_ERROR = "INTERNAL_ERROR";

  @Nonnull
  @ExceptionHandler(RpcException.class)
  public ResponseEntity<ApiErrorResponse> handleRpcException(@Nonnull final RpcException exception) {
    final var code = Optional.ofNullable(exception.getErrorCode()).orElse(INTERNAL_ERROR);
    final var status = mapStatus(code);

    if (status.is5xxServerError()) {
      LOGGER.error("Unhandled RPC error [{}]", code, exception);
    }

    return ResponseEntity.status(status).body(new ApiErrorResponse(code, exception.getMessage()));
  }

  @Nonnull
  private HttpStatus mapStatus(@Nonnull final String code) {
    if (DBNotFoundReason.BILLING_ACCESS_TOKEN_BY_TOKEN_HASH_NOT_FOUND.getMessage().equals(code)) {
      return HttpStatus.NOT_FOUND;
    } else if ("BILLING_TOKEN_EXPIRED".equals(code) || "BILLING_TOKEN_USED".equals(code)) {
      return HttpStatus.GONE;
    }
    return HttpStatus.INTERNAL_SERVER_ERROR;
  }
}