package ch.verno.common.db.dto.table;

import ch.verno.common.db.dto.base.BaseDto;
import ch.verno.publ.Publ;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.Objects;

public class CourseLevelDto extends BaseDto {

  @Nonnull
  private String code;

  @Nonnull
  private String name;

  @Nonnull
  private String description;

  @Nullable
  private Integer sortingOrder;

  public CourseLevelDto() {
    setId(null);
    this.code = Publ.EMPTY_STRING;
    this.name = Publ.EMPTY_STRING;
    this.description = Publ.EMPTY_STRING;
    this.sortingOrder = null;
  }

  public CourseLevelDto(@Nullable final Long id,
                        @Nonnull final String code,
                        @Nonnull final String name,
                        @Nonnull final String description,
                        @Nullable final Integer sortingOrder) {
    setId(id);
    this.code = code;
    this.name = name;
    this.description = description;
    this.sortingOrder = sortingOrder;
  }

  @Nonnull
  public static CourseLevelDto empty() {
    return new CourseLevelDto();
  }

  public boolean isEmpty() {
    return getId() != null
            && getId() == 0L
            && code.isEmpty()
            && name.isEmpty()
            && description.isEmpty()
            && sortingOrder == null;
  }

  @Nonnull
  public String getCode() {
    return code;
  }

  public void setCode(@Nonnull final String code) {
    this.code = code;
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

  @Nullable
  public Integer getSortingOrder() {
    return sortingOrder;
  }

  public void setSortingOrder(@Nullable final Integer sortingOrder) {
    this.sortingOrder = sortingOrder;
  }

  @Nonnull
  public String displayName() {
    return name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof CourseLevelDto other)) return false;
    return getId() != null && getId().equals(other.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId());
  }
}