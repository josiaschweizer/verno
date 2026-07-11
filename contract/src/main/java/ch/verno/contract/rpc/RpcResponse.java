package ch.verno.contract.rpc;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public record RpcResponse(boolean success,
                          @Nullable Object result,
                          @Nullable String errorCode,
                          @Nullable String errorMessage) {

  @Nonnull
  public static RpcResponse success(@Nullable final Object result) {
    return new RpcResponse(true, result, null, null);
  }

  @Nonnull
  public static RpcResponse error(final int code, @Nonnull final String message) {
    return new RpcResponse(false, null, String.valueOf(code), message);
  }

  @Nonnull
  public static RpcResponse error(@Nullable final String errorCode,
                                  @Nullable final String errorMessage) {
    return new RpcResponse(false, null, errorCode, errorMessage);
  }
}