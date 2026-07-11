package ch.verno.contract.api.exernal.email;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public record SendMailResponse(boolean success,
                               @Nullable String message) {

  @Nonnull
  public static SendMailResponse forSuccess() {
    return new SendMailResponse(true, null);
  }

}
