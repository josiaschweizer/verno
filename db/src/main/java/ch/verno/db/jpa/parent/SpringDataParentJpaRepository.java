package ch.verno.db.jpa.parent;

import ch.verno.db.entity.participant.ParentEntity;
import ch.verno.db.jpa.base.AbstractEntityJpaRepository;
import jakarta.annotation.Nonnull;

import java.util.List;
import java.util.Optional;

public interface SpringDataParentJpaRepository extends AbstractEntityJpaRepository<ParentEntity, Long> {

  @Nonnull
  Optional<ParentEntity> findByIdAndTenant_Id(@Nonnull Long id,
                                              @Nonnull Long tenantId);

  @Nonnull
  List<ParentEntity> findAllByTenant_Id(@Nonnull Long tenantId);

  @Nonnull
  Optional<ParentEntity> findByFirstnameAndLastnameAndEmailAndPhone(
          String firstname,
          String lastname,
          String email,
          String phone
  );
}
