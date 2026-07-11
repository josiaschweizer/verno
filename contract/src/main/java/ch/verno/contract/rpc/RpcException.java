package ch.verno.contract.rpc;

import jakarta.annotation.Nullable;

public class RpcException extends RuntimeException {

  @Nullable private String errorCode;

  public RpcException(@Nullable final String message) {
    super(message);
  }

  public RpcException(@Nullable final String errorCode,
                      @Nullable final String message) {
    super(message);
    this.errorCode = errorCode;
  }

  public RpcException(@Nullable final String message,
                      @Nullable final Throwable cause) {
    super(message, cause);
  }

  @Nullable
  public String getErrorCode() {
    return errorCode;
  }
}