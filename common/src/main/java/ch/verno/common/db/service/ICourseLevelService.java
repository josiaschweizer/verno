package ch.verno.common.db.service;

import ch.verno.common.db.dto.CourseLevelDto;
import jakarta.annotation.Nonnull;

import java.util.List;

public interface ICourseLevelService {

  @Nonnull
  CourseLevelDto getCourseLevelById(@Nonnull Long id);

  @Nonnull
  List<CourseLevelDto> getAllCourseLevels();
}
