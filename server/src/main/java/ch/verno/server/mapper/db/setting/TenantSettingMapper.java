package ch.verno.server.mapper.db.setting;

import ch.verno.contract.dto.table.setting.TenantSettingDto;
import ch.verno.db.entity.setting.TenantSettingEntity;
import ch.verno.server.mapper.db.base.IEntityMapper;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Component;

@Component
public class TenantSettingMapper implements IEntityMapper<TenantSettingEntity, TenantSettingDto> {

  @Nonnull
  @Override
  public TenantSettingDto toSimpleDto(@Nonnull final TenantSettingEntity entity) {
    final var dto = TenantSettingDto.empty();

    dto.setId(entity.getId());
    dto.setCourseDaysPerSchedule(entity.getCourseDaysPerSchedule());
    dto.setMaxParticipantsPerCourse(entity.getMaxParticipantsPerCourse());
    dto.setEnforceQuantitySettings(entity.isEnforceQuantitySettings());
    dto.setEnforceCourseLevelSettings(entity.isEnforceCourseLevelSettings());
    dto.setParentOneMainParent(entity.isParentOneMainParent());
    dto.setCourseReportName(entity.getCourseReportName());
    dto.setCourseReportTemplate(entity.getCourseReportTemplate());
    dto.setLimitCourseAssignmentsToActive(entity.isLimitCourseAssignmentsToActive());

    return dto;
  }

  @Nonnull
  @Override
  public TenantSettingEntity toNewEntity(@Nonnull final TenantSettingDto dto) {
    return new TenantSettingEntity(
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
  }

  @Override
  public void updateEntity(@Nonnull final TenantSettingEntity entity,
                           @Nonnull final TenantSettingDto dto) {
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