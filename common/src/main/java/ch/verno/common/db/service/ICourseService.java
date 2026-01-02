package ch.verno.common.db.service;

import ch.verno.common.db.dto.CourseDto;
import ch.verno.common.db.enums.CourseScheduleStatus;
import jakarta.annotation.Nonnull;

import java.util.List;

public interface ICourseService {

  @Nonnull
  CourseDto createCourse(@Nonnull final CourseDto courseDto);

  @Nonnull
  CourseDto updateCourse(@Nonnull final CourseDto courseDto);

  @Nonnull
  CourseDto getCourseById(@Nonnull Long id);

  @Nonnull
  List<CourseDto> getCoursesByCourseScheduleId(@Nonnull Long courseScheduleId);

  @Nonnull
  List<CourseDto> getCoursesByCourseScheduleStatus(@Nonnull CourseScheduleStatus status);

  @Nonnull
  List<CourseDto> getAllCourses();
}
