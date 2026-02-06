package ch.verno.server.mapper;

import ch.verno.common.db.dto.table.MandantSettingDto;
import ch.verno.db.entity.setting.MandantSettingEntity;
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
            null,
            dto.getCourseDaysPerSchedule(),
            dto.getMaxParticipantsPerCourse(),
            dto.isEnforceQuantitySettings(),
            dto.isEnforceCourseLevelSettings(),
            dto.isParentOneMainParent(),
            dto.getCourseReportName(),
            dto.isLimitCourseAssignmentsToActive()
    );

    if (dto.getMandantId() != null) {
      // we don't import MandantEntity here; caller/service should set a managed Mandant reference before saving
      entity.setId(dto.getMandantId());
    } else if (dto.getId() != null) {
      entity.setId(dto.getId());
    }

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
  }
}