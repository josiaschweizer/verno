package ch.verno.common.db.dto;

import ch.verno.common.util.Publ;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public record CourseLevelDto(@Nullable Long id,
                             @Nonnull String code,
                             @Nonnull String name,
                             @Nonnull String description,
                             @Nullable Integer sortingOrder) {

  public static CourseLevelDto empty() {
    return new CourseLevelDto(
        0L,
        Publ.EMPTY_STRING,
        Publ.EMPTY_STRING,
        Publ.EMPTY_STRING,
        null
    );
  }

  public boolean isEmpty() {
    return this.id() != null
        && this.id() == 0L
        && this.code().isEmpty()
        && this.name().isEmpty()
        && this.description().isEmpty()
        && this.sortingOrder() == null;
  }

  public String displayName() {
    return name;
  }
}