package ch.verno.server.service.intern;

import ch.verno.common.db.dto.defaultdto.DefaultTenantSettingDto;
import ch.verno.common.db.dto.table.TenantSettingDto;
import ch.verno.common.server.service.intern.tenant.ITenantSettingService;
import ch.verno.common.tenant.TenantContext;
import ch.verno.db.entity.setting.TenantSettingEntity;
import ch.verno.db.entity.tenant.TenantEntity;
import ch.verno.server.mapper.TenantSettingMapper;
import ch.verno.server.repository.TenantSettingRepository;
import jakarta.annotation.Nonnull;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class TenantSettingService implements ITenantSettingService {

  @PersistenceContext private EntityManager em;
  @Nonnull private final TenantSettingRepository repository;

  public TenantSettingService(@Nonnull final TenantSettingRepository repository) {
    this.repository = repository;
  }

  @Nonnull
  @Override
  @Transactional(readOnly = true)
  public TenantSettingDto getCurrentTenantSettingOrDefault() {
    return getCurrentTenantSetting()
            .orElseGet(() ->
                    DefaultTenantSettingDto.getDefault(TenantContext.getRequired())
            );
  }

  @Nonnull
  @Override
  @Transactional(readOnly = true)
  public Optional<TenantSettingDto> getCurrentTenantSetting() {
    final var tenantId = TenantContext.getRequired();

    return repository.findById(tenantId)
            .map(TenantSettingMapper::toDto);
  }

  @Nonnull
  @Override
  @Transactional
  public TenantSettingDto getOrCreateCurrentTenantSetting(@Nonnull final TenantSettingDto defaultDto) {
    final long tenantId = TenantContext.getRequired();

    return repository.findById(tenantId)
            .map(TenantSettingMapper::toDto)
            .orElseGet(() -> saveCurrentTenantSetting(DefaultTenantSettingDto.getDefault(tenantId)));
  }

  @Nonnull
  @Override
  @Transactional
  public TenantSettingDto saveCurrentTenantSetting(@Nonnull final TenantSettingDto dto) {
    final long tenantId = TenantContext.getRequired();
    final TenantEntity tenantRef = em.getReference(TenantEntity.class, tenantId);

    final TenantSettingEntity entity = repository.findById(tenantId)
            .orElseGet(() -> new TenantSettingEntity(
                    tenantRef,
                    dto.getCourseDaysPerSchedule(),
                    dto.getMaxParticipantsPerCourse(),
                    dto.isEnforceQuantitySettings(),
                    dto.isEnforceCourseLevelSettings(),
                    dto.isParentOneMainParent(),
                    dto.getCourseReportName(),
                    dto.getCourseReportTemplate(),
                    dto.isLimitCourseAssignmentsToActive()
            ));

    if (entity.getTenant() == null) {
      entity.setTenant(tenantRef);
    }

    TenantSettingMapper.updateEntity(dto, entity);

    final var saved = repository.save(entity);
    return TenantSettingMapper.toDto(saved);
  }
}