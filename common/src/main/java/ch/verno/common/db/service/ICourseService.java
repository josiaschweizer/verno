package ch.verno.common.db.service;

import ch.verno.common.db.dto.CourseDto;
import jakarta.annotation.Nonnull;

import java.util.List;
import java.util.Optional;

public interface ICourseService {

  @Nonnull
  Optional<CourseDto> getCourseById(@Nonnull Long id);

  @Nonnull
  List<CourseDto> getAllCourses();
}
