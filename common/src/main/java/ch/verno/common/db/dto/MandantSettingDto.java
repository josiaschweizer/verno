package ch.verno.common.db.dto;

import ch.verno.common.db.dto.base.BaseDto;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public class MandantSettingDto extends BaseDto {

  @Nonnull
  private Integer quantityOfCoursesPerSchedule;

  public MandantSettingDto() {
    this(null, 0);
  }

  public MandantSettingDto(@Nullable final Long id,
                           @Nonnull final Integer quantityOfCoursesPerSchedule) {
    setId(id);
    this.quantityOfCoursesPerSchedule = quantityOfCoursesPerSchedule;
  }

  @Nonnull
  public Integer getQuantityOfCoursesPerSchedule() {
    return quantityOfCoursesPerSchedule;
  }

  public void setQuantityOfCoursesPerSchedule(@Nonnull final Integer quantityOfCoursesPerSchedule) {
    this.quantityOfCoursesPerSchedule = quantityOfCoursesPerSchedule;
  }
}