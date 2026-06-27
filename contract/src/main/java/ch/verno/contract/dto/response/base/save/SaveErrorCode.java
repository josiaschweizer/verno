package ch.verno.contract.dto.response.base.save;

import jakarta.annotation.Nonnull;

public enum SaveErrorCode {
  NOT_FOUND("server.the.entity.could.not.be.found.with.the.given.id"),
  DATABASE_ERROR("server.a.database.error.occurred"),
  EMAIL_ALREADY_EXISTS("server.email.already.exists"),
  ;

  @Nonnull private final String translationKey;

  SaveErrorCode(@Nonnull final String translationKey) {
    this.translationKey = translationKey;
  }

  @Nonnull
  public String getTranslationKey() {
    return translationKey;
  }
}
