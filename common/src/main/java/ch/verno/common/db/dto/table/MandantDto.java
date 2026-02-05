package ch.verno.common.db.dto.table;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public final class MandantDto {

  private final Long id;
  private final String slug;
  private final String name;

  public MandantDto(@Nonnull final Long id,
                    @Nonnull final String slug,
                    @Nullable final String name) {
    this.id = id;
    this.slug = slug;
    this.name = name;
  }

  @Nonnull
  public Long getId() {
    return id;
  }

  @Nonnull
  public String getSlug() {
    return slug;
  }

  @Nullable
  public String getName() {
    return name;
  }
  @Override
  public String toString() {
    if (name == null || name.isBlank()) {
      return slug + " (ID: " + id + ")";
    }
    return name + " [" + slug + ", ID: " + id + "]";
  }
}