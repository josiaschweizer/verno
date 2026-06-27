package ch.verno.server.repository.gender;

import ch.verno.db.entity.gender.GenderEntity;
import ch.verno.db.jpa.gender.SpringDataGenderJpaRepository;
import ch.verno.server.repository.base.AbstractEntityRepository;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class GenderRepository extends AbstractEntityRepository<GenderEntity, Long, SpringDataGenderJpaRepository> {

  protected GenderRepository(@Nonnull final SpringDataGenderJpaRepository repository) {
    super(repository);
  }

  @Nonnull
  public Optional<GenderEntity> findByName(@Nonnull final String name) {
    return getRepository().findByName(name);
  }

}