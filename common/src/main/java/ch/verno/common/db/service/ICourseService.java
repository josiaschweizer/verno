package ch.verno.common.db.service;

import ch.verno.common.db.dto.response.DeleteResponseDto;
import ch.verno.common.db.dto.table.CourseDto;
import ch.verno.common.db.enums.CourseScheduleStatus;
import ch.verno.common.db.filter.CourseFilter;
import com.vaadin.flow.data.provider.QuerySortOrder;
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

  @Nonnull
  DeleteResponseDto delete(@Nonnull CourseDto course);

  @Nonnull
  List<CourseDto> findCourses(@Nonnull CourseFilter filter,
                              int offset,
                              int limit,
                              @Nonnull List<QuerySortOrder> sortOrders);

  int countCourses(@Nonnull CourseFilter filter);

  boolean canDelete(@Nonnull CourseDto course);
}
