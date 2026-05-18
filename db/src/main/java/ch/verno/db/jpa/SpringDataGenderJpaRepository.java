package ch.verno.db.jpa;

import ch.verno.db.entity.GenderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.annotation.Nonnull;
import java.util.Optional;

public interface SpringDataGenderJpaRepository extends JpaRepository<GenderEntity, Long> {

  @Nonnull
  Optional<GenderEntity> findByName(@Nonnull String name);

}
