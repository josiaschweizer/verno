package ch.verno.server.repository.participant;

import ch.verno.db.entity.participant.ParentEntity;
import ch.verno.db.jpa.parent.SpringDataParentJpaRepository;
import ch.verno.server.repository.base.AbstractEntityRepository;
import jakarta.annotation.Nonnull;

import java.util.Optional;

public class ParentRepository extends AbstractEntityRepository<
        ParentEntity,
        Long,
        SpringDataParentJpaRepository> {

  protected ParentRepository(@Nonnull final SpringDataParentJpaRepository repository) {
    super(repository);
  }

  @Nonnull
  public Optional<ParentEntity> findByFields(@Nonnull final String firstname,
                                             @Nonnull final String lastname,
                                             @Nonnull final String email,
                                             @Nonnull final String phone) {
    return getRepository().findByFirstnameAndLastnameAndEmailAndPhone(firstname, lastname, email, phone);
  }
}
