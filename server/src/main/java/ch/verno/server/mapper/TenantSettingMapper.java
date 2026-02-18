package ch.verno.server.mapper;

import ch.verno.common.db.dto.table.TenantSettingDto;
import ch.verno.db.entity.setting.TenantSettingEntity;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Component;

@Component
public class TenantSettingMapper {

  @Nonnull
  public static TenantSettingDto toDto(@Nonnull final TenantSettingEntity entity) {
    final var dto = new TenantSettingDto(
            entity.getId(),
            entity.getCourseDaysPerSchedule(),
            entity.getMaxParticipantsPerCourse(),
            entity.isEnforceQuantitySettings(),
            entity.isEnforceCourseLevelSettings(),
            entity.isParentOneMainParent(),
            entity.getCourseReportName(),
            entity.getCourseReportTemplate(),
            entity.isLimitCourseAssignmentsToActive()
    );

    if (entity.getTenant() != null) {
      dto.setTenantId(entity.getTenant().getId());
    }

    return dto;
  }

  @Nonnull
  public static TenantSettingEntity toEntity(@Nonnull final TenantSettingDto dto) {
    final var entity = new TenantSettingEntity(
            null,
            dto.getCourseDaysPerSchedule(),
            dto.getMaxParticipantsPerCourse(),
            dto.isEnforceQuantitySettings(),
            dto.isEnforceCourseLevelSettings(),
            dto.isParentOneMainParent(),
            dto.getCourseReportName(),
            dto.getCourseReportTemplate(),
            dto.isLimitCourseAssignmentsToActive()
    );

    if (dto.getTenantId() != null) {
      // we don't import TenantEntity here; caller/service should set a managed Tenant reference before saving
      entity.setId(dto.getTenantId());
    } else if (dto.getId() != null) {
      entity.setId(dto.getId());
    }

    return entity;
  }

  public static void updateEntity(@Nonnull final TenantSettingDto dto,
                                  @Nonnull final TenantSettingEntity entity) {
    entity.setCourseDaysPerSchedule(dto.getCourseDaysPerSchedule());
    entity.setMaxParticipantsPerCourse(dto.getMaxParticipantsPerCourse());
    entity.setEnforceQuantitySettings(dto.isEnforceQuantitySettings());
    entity.setEnforceCourseLevelSettings(dto.isEnforceCourseLevelSettings());
    entity.setParentOneMainParent(dto.isParentOneMainParent());
    entity.setCourseReportName(dto.getCourseReportName());
    entity.setCourseReportTemplate(dto.getCourseReportTemplate());
    entity.setLimitCourseAssignmentsToActive(dto.isLimitCourseAssignmentsToActive());
  }
}