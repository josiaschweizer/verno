package ch.verno.server.mapper.course;

import ch.verno.contract.dto.table.course.CourseDto;
import ch.verno.contract.dto.table.course.CourseLevelDto;
import ch.verno.contract.dto.table.course.CourseScheduleDto;
import ch.verno.contract.dto.table.instructor.InstructorDto;
import ch.verno.db.entity.course.CourseEntity;
import ch.verno.db.entity.course.CourseLevelEntity;
import ch.verno.db.entity.course.CourseScheduleEntity;
import ch.verno.db.entity.instructor.InstructorEntity;
import ch.verno.server.mapper.base.IEntityMapper;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Component;

@Component
public class CourseMapper implements IEntityMapper<CourseEntity, CourseDto> {

  @Nonnull
  @Override
  public CourseDto toSimpleDto(@Nonnull final CourseEntity entity) {
    final var dto = CourseDto.empty();

    dto.setId(entity.getId());
    dto.setTenantId(entity.getTenant() != null ? entity.getTenant().getId() : null);
    dto.setTitle(entity.getTitle());
    dto.setCapacity(entity.getCapacity());
    dto.setLocation(entity.getLocation());
    dto.setCourseLevels(entity.getCourseLevels()
            .stream()
            .map(CourseLevelEntity::getId)
            .map(CourseLevelDto::ref)
            .toList()
    );
    dto.setCourseSchedule(entity.getCourseSchedule() == null
            ? CourseScheduleDto.empty()
            : CourseScheduleDto.ref(entity.getCourseSchedule().getId())
    );
    dto.setWeekdays(entity.getWeekdays());
    dto.setStartTime(entity.getStartTime());
    dto.setEndTime(entity.getEndTime());
    dto.setInstructor(entity.getInstructor() == null
            ? InstructorDto.empty()
            : InstructorDto.ref(entity.getInstructor().getId())
    );
    dto.setSecondaryInstructors(entity.getSecondaryInstructors()
            .stream()
            .map(InstructorEntity::getId)
            .map(InstructorDto::ref)
            .toList()
    );
    dto.setNote(entity.getNote());
    dto.setColor(entity.getColor());

    return dto;
  }

  @Nonnull
  @Override
  public CourseEntity toNewEntity(@Nonnull final CourseDto dto) {
    final var entity = CourseEntity.empty();
    updateEntity(entity, dto);
    return entity;
  }

  @Override
  public void updateEntity(@Nonnull final CourseEntity entity,
                           @Nonnull final CourseDto dto) {
    entity.setTitle(dto.getTitle());
    entity.setCapacity(dto.getCapacity());
    entity.setLocation(dto.getLocation());
    entity.setCourseLevels(dto.getCourseLevels()
            .stream()
            .filter(level -> level.getId() != null)
            .map(level -> CourseLevelEntity.ref(level.getId()))
            .toList()
    );
    entity.setCourseSchedule(dto.getCourseSchedule() == null || dto.getCourseSchedule().getId() == null
            ? null
            : CourseScheduleEntity.ref(dto.getCourseSchedule().getId())
    );
    entity.setWeekdays(dto.getWeekdays());
    entity.setStartTime(dto.getStartTime());
    entity.setEndTime(dto.getEndTime());
    entity.setInstructor(dto.getInstructor() == null || dto.getInstructor().getId() == null
            ? null
            : InstructorEntity.ref(dto.getInstructor().getId())
    );
    entity.setSecondaryInstructors(dto.getSecondaryInstructors()
            .stream()
            .filter(instructor -> instructor.getId() != null)
            .map(instructor -> InstructorEntity.ref(instructor.getId()))
            .toList()
    );
    entity.setNote(dto.getNote());
    entity.setColor(dto.getColor());
  }
}