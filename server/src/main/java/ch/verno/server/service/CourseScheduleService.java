package ch.verno.server.service;

import ch.verno.common.db.dto.CourseScheduleDto;
import ch.verno.common.db.service.ICourseScheduleService;
import ch.verno.common.exceptions.NotFoundException;
import ch.verno.common.exceptions.NotFoundReason;
import ch.verno.server.mapper.CourseScheduleMapper;
import ch.verno.server.repository.CourseScheduleRepository;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseScheduleService implements ICourseScheduleService {

  @Nonnull
  private final CourseScheduleRepository courseScheduleRepository;

  public CourseScheduleService(@Nonnull final CourseScheduleRepository courseScheduleRepository) {
    this.courseScheduleRepository = courseScheduleRepository;
  }

  @Nonnull
  @Override
  public CourseScheduleDto createCourseSchedule(@Nonnull final CourseScheduleDto courseScheduleDto) {
    return null;
  }

  @Nonnull
  @Override
  public CourseScheduleDto updateCourseSchedule(@Nonnull final CourseScheduleDto courseScheduleDto) {
    return null;
  }

  @Nonnull
  @Override
  public CourseScheduleDto getCourseScheduleById(@Nonnull final Long id) {
    final var foundById = courseScheduleRepository.findById(id);
    if (foundById.isEmpty()) {
      throw new NotFoundException(NotFoundReason.COURSE_SCHEDULE_BY_ID_NOT_FOUND, id);
    }

    return CourseScheduleMapper.toDto(foundById.get());
  }

  @Nonnull
  @Override
  public List<CourseScheduleDto> getAllCourseSchedules() {
    return courseScheduleRepository.findAll()
            .stream()
            .map(CourseScheduleMapper::toDto)
            .toList();
  }
}
