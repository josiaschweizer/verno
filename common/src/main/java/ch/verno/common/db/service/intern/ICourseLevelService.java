package ch.verno.common.db.service.intern;

import ch.verno.common.db.dto.table.CourseLevelDto;
import jakarta.annotation.Nonnull;

import java.util.List;
import java.util.Optional;

public interface ICourseLevelService {

  @Nonnull
  CourseLevelDto createCourseLevel(@Nonnull CourseLevelDto courseLevelDto);

  @Nonnull
  CourseLevelDto updateCourseLevel(@Nonnull CourseLevelDto courseLevelDto);

  @Nonnull
  CourseLevelDto getCourseLevelById(@Nonnull Long id);

  @Nonnull
  List<CourseLevelDto> getAllCourseLevels();

  @Nonnull
  Optional<CourseLevelDto> getCourseLevelByCode(@Nonnull String code);
}
