package ch.verno.server.repository;

import ch.verno.db.entity.mandant.MandantSettingEntity;
import ch.verno.db.jpa.SpringDataMandantSettingJpaRepository;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MandantSettingRepository {

  @Nonnull
  private final SpringDataMandantSettingJpaRepository springDataMandantSettingJpaRepository;

  public MandantSettingRepository(@Nonnull final SpringDataMandantSettingJpaRepository springDataMandantSettingJpaRepository) {
    this.springDataMandantSettingJpaRepository = springDataMandantSettingJpaRepository;
  }

  @Nonnull
  public Optional<MandantSettingEntity> findById(@Nonnull final Long id) {
    return springDataMandantSettingJpaRepository.findById(id);
  }

  @Nonnull
  public List<MandantSettingEntity> findAll() {
    return springDataMandantSettingJpaRepository.findAll();
  }

  @Nonnull
  public MandantSettingEntity save(@Nonnull final MandantSettingEntity entity) {
    return springDataMandantSettingJpaRepository.save(entity);
  }


}
