package ch.verno.contract.dto.file.storage;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public record DeletedObjectDto(boolean successful,
                               @Nullable String reason) {

  @Nonnull
  public static DeletedObjectDto successfully() {
    return new DeletedObjectDto(true, null);
  }

  @Nonnull
  public static DeletedObjectDto faulty() {
    return new DeletedObjectDto(false, null);
  }

  @Nonnull
  public static DeletedObjectDto faulty(@Nonnull String reason) {
    return new DeletedObjectDto(false, reason);
  }

}
