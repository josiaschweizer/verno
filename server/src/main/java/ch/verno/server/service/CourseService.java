package ch.verno.server.service;

import ch.verno.common.db.dto.CourseDto;
import ch.verno.common.db.enums.CourseScheduleStatus;
import ch.verno.common.db.filter.CourseFilter;
import ch.verno.common.db.service.ICourseService;
import ch.verno.common.exceptions.db.DBNotFoundException;
import ch.verno.common.exceptions.db.DBNotFoundReason;
import ch.verno.db.entity.CourseEntity;
import ch.verno.server.mapper.CourseMapper;
import ch.verno.server.repository.CourseLevelRepository;
import ch.verno.server.repository.CourseRepository;
import ch.verno.server.repository.CourseScheduleRepository;
import ch.verno.server.repository.InstructorRepository;
import ch.verno.server.spec.CourseSpec;
import ch.verno.server.spec.PageHelper;
import com.vaadin.flow.data.provider.QuerySortOrder;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CourseService implements ICourseService {

  @Nonnull
  private final CourseRepository courseRepository;
  @Nonnull
  private final CourseLevelRepository courseLevelRepository;
  @Nonnull
  private final CourseScheduleRepository courseScheduleRepository;
  @Nonnull
  private final InstructorRepository instructorRepository;

  @Nonnull
  private final ServiceHelper serviceHelper;
  @Nonnull
  private final CourseSpec courseSpec;

  public CourseService(@Nonnull final CourseRepository courseRepository,
                       @Nonnull final CourseLevelRepository courseLevelRepository,
                       @Nonnull final CourseScheduleRepository courseScheduleRepository,
                       @Nonnull final InstructorRepository instructorRepository) {
    this.courseRepository = courseRepository;
    this.courseLevelRepository = courseLevelRepository;
    this.courseScheduleRepository = courseScheduleRepository;
    this.instructorRepository = instructorRepository;

    this.serviceHelper = new ServiceHelper();
    this.courseSpec = new CourseSpec();
  }

  @Nonnull
  @Override
  @Transactional
  public CourseDto createCourse(@Nonnull final CourseDto courseDto) {
    final var levels = serviceHelper.resolveCourseLevels(courseLevelRepository, courseDto.getCourseLevels());
    if (levels.isEmpty()) {
      throw new IllegalArgumentException("At least one course level is required");
    }

    final var schedule = serviceHelper.resolveCourseSchedule(courseScheduleRepository, courseDto.getCourseSchedule());
    if (schedule == null) {
      throw new IllegalArgumentException("Course schedule is required");
    }

    final var entity = new CourseEntity(
            ServiceHelper.safeString(courseDto.getTitle()),
            courseDto.getCapacity(),
            ServiceHelper.safeString(courseDto.getLocation()),
            levels,
            schedule,
            courseDto.getWeekdays(),
            courseDto.getStartTime(),
            courseDto.getEndTime(),
            serviceHelper.resolveInstructor(instructorRepository, courseDto.getInstructor())
    );

    entity.setId(null);

    final var saved = courseRepository.save(entity);
    return CourseMapper.toDto(saved);
  }

  @Nonnull
  @Override
  @Transactional
  public CourseDto updateCourse(@Nonnull final CourseDto courseDto) {
    if (courseDto.getId() == null || courseDto.getId() == 0L) {
      throw new IllegalArgumentException("Course ID is required for update");
    }

    final var existing = courseRepository.findById(courseDto.getId())
            .orElseThrow(() -> new DBNotFoundException(DBNotFoundReason.COURSE_BY_ID_NOT_FOUND, courseDto.getId()));

    existing.setTitle(ServiceHelper.safeString(courseDto.getTitle()));
    existing.setCapacity(courseDto.getCapacity());
    existing.setLocation(ServiceHelper.safeString(courseDto.getLocation()));
    existing.setStartTime(courseDto.getStartTime());
    existing.setEndTime(courseDto.getEndTime());

    final var levels = serviceHelper.resolveCourseLevels(courseLevelRepository, courseDto.getCourseLevels());
    if (levels.isEmpty()) {
      throw new IllegalArgumentException("At least one course level is required");
    }

    final var schedule = serviceHelper.resolveCourseSchedule(courseScheduleRepository, courseDto.getCourseSchedule());
    if (schedule == null) {
      throw new IllegalArgumentException("Course schedule is required");
    }

    existing.setCourseLevels(levels);
    existing.setCourseSchedule(schedule);
    existing.setWeekdays(courseDto.getWeekdays());
    existing.setInstructor(serviceHelper.resolveInstructor(instructorRepository, courseDto.getInstructor()));

    final var saved = courseRepository.save(existing);
    return CourseMapper.toDto(saved);
  }

  @Nonnull
  @Transactional(readOnly = true)
  @Override
  public CourseDto getCourseById(@Nonnull final Long id) {
    final var foundById = courseRepository.findById(id);
    if (foundById.isEmpty()) {
      throw new DBNotFoundException(DBNotFoundReason.COURSE_BY_ID_NOT_FOUND, id);
    }

    return CourseMapper.toDto(foundById.get());
  }

  @Nonnull
  @Override
  @Transactional
  public List<CourseDto> getCoursesByCourseScheduleId(@Nonnull final Long courseScheduleId) {
    return courseRepository.findByCourseLevelId(courseScheduleId).stream()
            .map(CourseMapper::toDto)
            .toList();
  }

  @Nonnull
  @Override
  @Transactional
  public List<CourseDto> getCoursesByCourseScheduleStatus(@Nonnull final CourseScheduleStatus status) {
    final var courseSchedules = courseScheduleRepository.findByStatus(status);

    return courseSchedules.stream()
            .flatMap(schedule -> getCoursesByCourseScheduleId(schedule.getId()).stream())
            .toList();
  }

  @Nonnull
  @Override
  @Transactional(readOnly = true)
  public List<CourseDto> getAllCourses() {
    return courseRepository.findAll().stream().map(CourseMapper::toDto).toList();
  }

  @Nonnull
  @Transactional(readOnly = true)
  public List<CourseDto> findCourses(@Nonnull final CourseFilter filter,
                                     final int offset,
                                     final int limit,
                                     @Nonnull final List<QuerySortOrder> sortOrders) {
    final var pageable = PageHelper.createPageRequest(offset, limit, sortOrders);
    final var spec = courseSpec.courseSpec(filter);

    return courseRepository.findAll(spec, pageable).stream()
            .map(CourseMapper::toDto)
            .toList();
  }

  @Transactional(readOnly = true)
  public int countCourses(@Nonnull final CourseFilter filter) {
    return Math.toIntExact(courseRepository.count(courseSpec.courseSpec(filter)));
  }
}