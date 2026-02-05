package ch.verno.server.service.intern;

import ch.verno.common.db.dto.defaultdto.DefaultMandantSettingDto;
import ch.verno.common.db.dto.table.MandantSettingDto;
import ch.verno.common.db.service.IMandantSettingService;
import ch.verno.common.mandant.MandantContext;
import ch.verno.db.entity.mandant.MandantEntity;
import ch.verno.db.entity.setting.MandantSettingEntity;
import ch.verno.server.mapper.MandantSettingMapper;
import ch.verno.server.repository.MandantSettingRepository;
import jakarta.annotation.Nonnull;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MandantSettingService implements IMandantSettingService {

  @Nonnull
  private final MandantSettingRepository repo;

  @PersistenceContext
  private EntityManager em;

  public MandantSettingService(@Nonnull final MandantSettingRepository repo) {
    this.repo = repo;
  }

  @Nonnull
  @Override
  @Transactional(readOnly = true)
  public MandantSettingDto getCurrentMandantSettingOrDefault() {
    final long mandantId = MandantContext.getRequired();

    return repo.findById(mandantId)
            .map(MandantSettingMapper::toDto)
            .orElseGet(() -> DefaultMandantSettingDto.getDefault(mandantId));
  }

  @Nonnull
  @Override
  @Transactional
  public MandantSettingDto getOrCreateCurrentMandantSetting(@Nonnull final MandantSettingDto defaultDto) {
    final long mandantId = MandantContext.getRequired();

    return repo.findById(mandantId)
            .map(MandantSettingMapper::toDto)
            .orElseGet(() -> saveCurrentMandantSetting(DefaultMandantSettingDto.getDefault(mandantId)));
  }

  @Nonnull
  @Override
  @Transactional
  public MandantSettingDto saveCurrentMandantSetting(@Nonnull final MandantSettingDto dto) {
    final long mandantId = MandantContext.getRequired();
    final MandantEntity mandantRef = em.getReference(MandantEntity.class, mandantId);

    final MandantSettingEntity entity = repo.findById(mandantId)
            .orElseGet(() -> new MandantSettingEntity(
                    mandantRef,
                    dto.getCourseDaysPerSchedule(),
                    dto.getMaxParticipantsPerCourse(),
                    dto.isEnforceQuantitySettings(),
                    dto.isEnforceCourseLevelSettings(),
                    dto.isParentOneMainParent(),
                    dto.getCourseReportName(),
                    dto.isLimitCourseAssignmentsToActive()
            ));

    if (entity.getMandant() == null) {
      entity.setMandant(mandantRef);
    }

    MandantSettingMapper.updateEntity(dto, entity);

    final var saved = repo.save(entity);
    return MandantSettingMapper.toDto(saved);
  }
}