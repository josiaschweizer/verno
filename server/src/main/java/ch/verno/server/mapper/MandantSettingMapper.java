package ch.verno.server.mapper;

import ch.verno.common.db.dto.MandantSettingDto;
import ch.verno.db.entity.mandant.MandantSettingEntity;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Component;

@Component
public class MandantSettingMapper {

  @Nonnull
  public static MandantSettingDto toDto(@Nonnull final MandantSettingEntity entity) {
    return new MandantSettingDto(
            entity.getId(),
            entity.getCourseDaysPerSchedule(),
            entity.getMaxParticipantsPerCourse(),
            entity.isEnforceQuantitySettings(),
            entity.isEnforceCourseLevelSettings(),
            entity.isParentOneMainParent()
    );
  }

  @Nonnull
  public static MandantSettingEntity toEntity(@Nonnull final MandantSettingDto dto) {
    final var entity = new MandantSettingEntity(
            dto.getCourseDaysPerSchedule(),
            dto.getMaxParticipantsPerCourse(),
            dto.isEnforceQuantitySettings(),
            dto.isEnforceCourseLevelSettings(),
            dto.isParentOneMainParent()
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
  }
}