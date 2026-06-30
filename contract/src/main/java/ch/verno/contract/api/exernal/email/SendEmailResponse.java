package ch.verno.contract.api.exernal.email;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public record SendEmailResponse(boolean success,
                                @Nullable String message) {

  @Nonnull
  public static SendEmailResponse forSuccess() {
    return new SendEmailResponse(true, null);
  }

}
