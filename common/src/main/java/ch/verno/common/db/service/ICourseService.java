package ch.verno.common.db.service;

import ch.verno.common.db.dto.CourseDto;
import jakarta.annotation.Nonnull;

import java.util.List;

public interface ICourseService {

  @Nonnull
  CourseDto getCourseById(@Nonnull Long id);

  @Nonnull
  List<CourseDto> getAllCourses();
}
