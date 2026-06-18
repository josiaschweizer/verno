package ch.verno.db.jpa.gender;

import ch.verno.db.entity.gender.GenderEntity;
import ch.verno.db.jpa.base.AbstractEntityJpaRepository;

import javax.annotation.Nonnull;
import java.util.Optional;

public interface SpringDataGenderJpaRepository extends AbstractEntityJpaRepository<GenderEntity, Long> {

  @Nonnull
  Optional<GenderEntity> findByName(@Nonnull String name);

}
