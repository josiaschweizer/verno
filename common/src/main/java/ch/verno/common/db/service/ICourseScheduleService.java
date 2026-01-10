package ch.verno.common.db.service;

import ch.verno.common.db.dto.CourseScheduleDto;
import ch.verno.common.db.enums.CourseScheduleStatus;
import ch.verno.common.db.filter.CourseScheduleFilter;
import com.vaadin.flow.data.provider.QuerySortOrder;
import jakarta.annotation.Nonnull;

import java.time.LocalDate;
import java.util.List;

public interface ICourseScheduleService {

  @Nonnull
  CourseScheduleDto createCourseSchedule(@Nonnull CourseScheduleDto courseScheduleDto);

  @Nonnull
  CourseScheduleDto updateCourseSchedule(@Nonnull CourseScheduleDto courseScheduleDto);

  @Nonnull
  CourseScheduleDto getCourseScheduleById(@Nonnull Long id);

  @Nonnull
  List<CourseScheduleDto> getCourseScheduleByWeek(@Nonnull LocalDate weekDate);

  @Nonnull
  List<CourseScheduleDto> getCourseSchedulesByStatus(@Nonnull CourseScheduleStatus status);

  @Nonnull
  List<CourseScheduleDto> getAllCourseSchedules();

  @Nonnull
  List<CourseScheduleDto> findCourseSchedules(@Nonnull CourseScheduleFilter filter,
                                              int offset,
                                              int limit,
                                              @Nonnull List<QuerySortOrder> sortOrders);

  int countCourseSchedules(@Nonnull CourseScheduleFilter filter);
}
