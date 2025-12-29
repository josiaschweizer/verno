package ch.verno.server.service;

import ch.verno.common.db.dto.CourseScheduleDto;
import ch.verno.common.db.filter.CourseScheduleFilter;
import ch.verno.common.db.service.ICourseScheduleService;
import ch.verno.common.exceptions.NotFoundException;
import ch.verno.common.exceptions.NotFoundReason;
import ch.verno.server.mapper.CourseScheduleMapper;
import ch.verno.server.repository.CourseScheduleRepository;
import ch.verno.server.spec.CourseScheduleSpec;
import ch.verno.server.spec.PageHelper;
import com.vaadin.flow.data.provider.QuerySortOrder;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CourseScheduleService implements ICourseScheduleService {

  @Nonnull
  private final CourseScheduleRepository courseScheduleRepository;
  @Nonnull
  private final CourseScheduleSpec courseScheduleSpec;

  public CourseScheduleService(@Nonnull final CourseScheduleRepository courseScheduleRepository) {
    this.courseScheduleRepository = courseScheduleRepository;

    this.courseScheduleSpec = new CourseScheduleSpec();
  }

  @Nonnull
  @Override
  @Transactional
  public CourseScheduleDto createCourseSchedule(@Nonnull final CourseScheduleDto courseScheduleDto) {
    final var entity = CourseScheduleMapper.toEntity(courseScheduleDto);
    if (entity == null) {
      throw new IllegalArgumentException("CourseScheduleDto is empty");
    }

    entity.setId(null);

    final var saved = courseScheduleRepository.save(entity);
    return CourseScheduleMapper.toDto(saved);
  }

  @Nonnull
  @Override
  @Transactional
  public CourseScheduleDto updateCourseSchedule(@Nonnull final CourseScheduleDto courseScheduleDto) {
    if (courseScheduleDto.getId() == null || courseScheduleDto.getId() == 0) {
      throw new IllegalArgumentException("CourseSchedule ID is required for update");
    }

    final var existing = courseScheduleRepository.findById(courseScheduleDto.getId())
            .orElseThrow(() -> new NotFoundException(
                    NotFoundReason.COURSE_SCHEDULE_BY_ID_NOT_FOUND,
                    courseScheduleDto.getId()
            ));

    final var updated = CourseScheduleMapper.toEntity(courseScheduleDto);
    if (updated == null) {
      throw new IllegalArgumentException("CourseScheduleDto is empty");
    }

    updated.setId(existing.getId());

    final var saved = courseScheduleRepository.save(updated);
    return CourseScheduleMapper.toDto(saved);
  }

  @Nonnull
  @Override
  @Transactional(readOnly = true)
  public CourseScheduleDto getCourseScheduleById(@Nonnull final Long id) {
    final var foundById = courseScheduleRepository.findById(id);
    if (foundById.isEmpty()) {
      throw new NotFoundException(NotFoundReason.COURSE_SCHEDULE_BY_ID_NOT_FOUND, id);
    }

    return CourseScheduleMapper.toDto(foundById.get());
  }

  @Nonnull
  @Override
  @Transactional(readOnly = true)
  public List<CourseScheduleDto> getAllCourseSchedules() {
    return courseScheduleRepository.findAll()
            .stream()
            .map(CourseScheduleMapper::toDto)
            .toList();
  }

  @Nonnull
  @Transactional(readOnly = true)
  public List<CourseScheduleDto> findCourseSchedules(@Nonnull final CourseScheduleFilter filter,
                                                     final int offset,
                                                     final int limit,
                                                     @Nonnull final List<QuerySortOrder> sortOrders) {
    final var pageable = PageHelper.createPageRequest(offset, limit, sortOrders);
    final var spec = courseScheduleSpec.courseScheduleSpec(filter);

    return courseScheduleRepository.findAll(spec, pageable).stream()
            .map(CourseScheduleMapper::toDto)
            .toList();
  }

  @Transactional(readOnly = true)
  public int countCourses(@Nonnull final CourseScheduleFilter filter) {
    return Math.toIntExact(courseScheduleRepository.count(courseScheduleSpec.courseScheduleSpec( filter)));
  }
}
