package ch.verno.contract.dto.response.base.delete;

import jakarta.annotation.Nonnull;

public enum DeleteErrorCode {
  ID_NULL("server.the.given.id.was.null"),
  ID_EMPTY("server.the.given.id.was.empty"),
  ;

  @Nonnull private final String translationKey;

  DeleteErrorCode(@Nonnull final String translationKey) {
    this.translationKey = translationKey;
  }

  @Nonnull
  public String getTranslationKey() {
    return translationKey;
  }
}
