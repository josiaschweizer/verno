package ch.verno.common.db.dto;

import ch.verno.common.db.dto.base.BaseDto;
import ch.verno.common.util.Publ;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public class MandantSettingDto extends BaseDto {

  @Nonnull
  private Integer courseDaysPerSchedule;
  @Nonnull
  private Integer maxParticipantsPerCourse;

  public MandantSettingDto() {
    this(null, Publ.ZERO, Publ.ZERO);
  }

  public MandantSettingDto(@Nullable final Long id,
                           @Nonnull final Integer courseDaysPerSchedule,
                           @Nonnull final Integer maxParticipantsPerCourse) {
    setId(id);
    this.courseDaysPerSchedule = courseDaysPerSchedule;
    this.maxParticipantsPerCourse = maxParticipantsPerCourse;
  }

  @Nonnull
  public Integer getCourseDaysPerSchedule() {
    return courseDaysPerSchedule;
  }

  public void setCourseDaysPerSchedule(@Nonnull final Integer courseDaysPerSchedule) {
    this.courseDaysPerSchedule = courseDaysPerSchedule;
  }

  @Nonnull
  public Integer getMaxParticipantsPerCourse() {
    return maxParticipantsPerCourse;
  }

  public void setMaxParticipantsPerCourse(@Nonnull final Integer maxParticipantsPerCourse) {
    this.maxParticipantsPerCourse = maxParticipantsPerCourse;
  }
}