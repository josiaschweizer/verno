package ch.verno.common.db.dto;

import ch.verno.common.db.dto.base.BaseDto;
import ch.verno.common.util.Publ;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.Objects;

public class GenderDto extends BaseDto {

  @Nonnull
  private String name;

  @Nonnull
  private String description;

  public GenderDto() {
    this(null,
            Publ.EMPTY_STRING,
            Publ.EMPTY_STRING
    );
  }

  public GenderDto(@Nullable final Long id,
                   @Nonnull final String name,
                   @Nonnull final String description) {
    setId(id);
    this.name = name;
    this.description = description;
  }

  @Nonnull
  public static GenderDto empty() {
    return new GenderDto(
            0L,
            Publ.EMPTY_STRING,
            Publ.EMPTY_STRING
    );
  }

  public boolean isEmpty() {
    return getId() != null
            && getId() == 0L
            && name.isEmpty();
  }

  @Nonnull
  public String getName() {
    return name;
  }

  public void setName(@Nonnull final String name) {
    this.name = name;
  }

  @Nonnull
  public String getDescription() {
    return description;
  }

  public void setDescription(@Nonnull final String description) {
    this.description = description;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof GenderDto other)) return false;
    return getId() != null && getId().equals(other.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId());
  }
}