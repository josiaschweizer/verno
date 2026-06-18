package ch.verno.contract.rpc;

import jakarta.annotation.Nullable;

public class RpcException extends RuntimeException {

  public RpcException(@Nullable final String message) {
    super(message);
  }

  public RpcException(@Nullable final String message,
                      @Nullable final Throwable cause) {
    super(message, cause);
  }

}