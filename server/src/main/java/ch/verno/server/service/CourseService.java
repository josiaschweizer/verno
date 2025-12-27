package ch.verno.server.service;

import ch.verno.common.db.dto.CourseDto;
import ch.verno.common.db.service.ICourseService;
import ch.verno.common.exceptions.NotFoundException;
import ch.verno.common.exceptions.NotFoundReason;
import ch.verno.db.entity.CourseEntity;
import ch.verno.server.mapper.CourseMapper;
import ch.verno.server.repository.CourseLevelRepository;
import ch.verno.server.repository.CourseRepository;
import ch.verno.server.repository.CourseScheduleRepository;
import ch.verno.server.repository.InstructorRepository;
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

  public CourseService(@Nonnull final CourseRepository courseRepository,
                       @Nonnull final CourseLevelRepository courseLevelRepository,
                       @Nonnull final CourseScheduleRepository courseScheduleRepository,
                       @Nonnull final InstructorRepository instructorRepository) {
    this.courseRepository = courseRepository;
    this.courseLevelRepository = courseLevelRepository;
    this.courseScheduleRepository = courseScheduleRepository;
    this.instructorRepository = instructorRepository;

    this.serviceHelper = new ServiceHelper();
  }

  @Nonnull
  @Override
  @Transactional
  public CourseDto createCourse(@Nonnull final CourseDto courseDto) {
    final var level = serviceHelper.resolveCourseLevel(courseLevelRepository, courseDto.getCourseLevel());
    if (level == null) {
      throw new IllegalArgumentException("Course level is required");
    }

    final var schedule = serviceHelper.resolveCourseSchedule(courseScheduleRepository, courseDto.getCourseSchedule());
    if (schedule == null) {
      throw new IllegalArgumentException("Course schedule is required");
    }

    final var entity = new CourseEntity(
            ServiceHelper.safeString(courseDto.getTitle()),
            courseDto.getCapacity(),
            ServiceHelper.safeString(courseDto.getLocation()),
            level,
            schedule,
            courseDto.getWeekdays(),
            courseDto.getDuration(),
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
            .orElseThrow(() -> new NotFoundException(NotFoundReason.COURSE_BY_ID_NOT_FOUND, courseDto.getId()));

    existing.setTitle(ServiceHelper.safeString(courseDto.getTitle()));
    existing.setCapacity(courseDto.getCapacity());
    existing.setLocation(ServiceHelper.safeString(courseDto.getLocation()));
    existing.setDuration(courseDto.getDuration());

    final var level = serviceHelper.resolveCourseLevel(courseLevelRepository, courseDto.getCourseLevel());
    if (level == null) {
      throw new IllegalArgumentException("Course level is required");
    }

    final var schedule = serviceHelper.resolveCourseSchedule(courseScheduleRepository, courseDto.getCourseSchedule());
    if (schedule == null) {
      throw new IllegalArgumentException("Course schedule is required");
    }

    existing.setLevel(level);
    existing.setSchedule(schedule);
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
      throw new NotFoundException(NotFoundReason.COURSE_BY_ID_NOT_FOUND, id);
    }

    return CourseMapper.toDto(foundById.get());
  }

  @Nonnull
  @Transactional(readOnly = true)
  @Override
  public List<CourseDto> getAllCourses() {
    return courseRepository.findAll().stream().map(CourseMapper::toDto).toList();
  }
}