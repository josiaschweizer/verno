package ch.verno.contract.api.util;

import ch.verno.lib.Publ;
import jakarta.annotation.Nonnull;

public record ApiErrorResponse(@Nonnull String code,
                               @Nonnull String message) {

  @Nonnull
  public static ApiErrorResponse simple(@Nonnull final String code) {
    return new ApiErrorResponse(code, Publ.EMPTY_STRING);
  }

}
