package ch.verno.server.mapper;

import ch.verno.common.db.dto.table.MandantSettingDto;
import ch.verno.db.entity.mandant.MandantEntity;
import ch.verno.db.entity.mandant.MandantSettingEntity;
import ch.verno.common.mandant.MandantContext;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Component;

@Component
public class MandantSettingMapper {

  @Nonnull
  public static MandantSettingDto toDto(@Nonnull final MandantSettingEntity entity) {
    final var dto = new MandantSettingDto(
            entity.getId(),
            entity.getCourseDaysPerSchedule(),
            entity.getMaxParticipantsPerCourse(),
            entity.isEnforceQuantitySettings(),
            entity.isEnforceCourseLevelSettings(),
            entity.isParentOneMainParent(),
            entity.getCourseReportName(),
            entity.isLimitCourseAssignmentsToActive()
    );

    if (entity.getMandant() != null) {
      dto.setMandantId(entity.getMandant().getId());
    }

    return dto;
  }

  @Nonnull
  public static MandantSettingEntity toEntity(@Nonnull final MandantSettingDto dto) {
    final var entity = new MandantSettingEntity(
            MandantEntity.ref(MandantContext.getRequired()),
            dto.getCourseDaysPerSchedule(),
            dto.getMaxParticipantsPerCourse(),
            dto.isEnforceQuantitySettings(),
            dto.isEnforceCourseLevelSettings(),
            dto.isParentOneMainParent(),
            dto.getCourseReportName(),
            dto.isLimitCourseAssignmentsToActive()
    );

    entity.setId(dto.getId());
    return entity;
  }

  public static void updateEntity(@Nonnull final MandantSettingDto dto,
                                  @Nonnull final MandantSettingEntity entity) {
    entity.setCourseDaysPerSchedule(dto.getCourseDaysPerSchedule());
    entity.setMaxParticipantsPerCourse(dto.getMaxParticipantsPerCourse());
    entity.setEnforceQuantitySettings(dto.isEnforceQuantitySettings());
    entity.setEnforceCourseLevelSettings(dto.isEnforceCourseLevelSettings());
    entity.setParentOneMainParent(dto.isParentOneMainParent());
    entity.setCourseReportName(dto.getCourseReportName());
    entity.setLimitCourseAssignmentsToActive(dto.isLimitCourseAssignmentsToActive());

    // do not change mandant here - mandant is tied to the entity identity
  }
}