package ch.verno.common.db.dto.response;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public record DeleteResponseDto(boolean success,
                                boolean hasDeleted,
                                @Nullable String message) {

  @Nonnull
  public static DeleteResponseDto conditionalSuccess(boolean success, boolean hasDeleted) {
    return new DeleteResponseDto(success, hasDeleted, null);
  }

  @Nonnull
  public static DeleteResponseDto unconditionalSuccess() { // methode cannot be named success due to var success
    return new DeleteResponseDto(true, true, null);
  }

  @Nonnull
  public static DeleteResponseDto failure(@Nullable String message) {
    return new DeleteResponseDto(false, false, message);
  }

}
