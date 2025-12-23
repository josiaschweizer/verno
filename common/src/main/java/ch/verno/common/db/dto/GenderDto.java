package ch.verno.common.db.dto;

import ch.verno.common.util.Publ;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public record GenderDto(
    @Nullable Long id,
    @Nonnull String name,
    @Nonnull String description) {

  public static GenderDto empty() {
    return new GenderDto(0L, Publ.EMPTY_STRING, Publ.EMPTY_STRING);
  }

  public boolean isEmpty() {
    return this.id() != null
        && this.id() == 0L
        && this.name().isEmpty();
  }
}
