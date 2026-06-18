package ch.verno.db.jpa.parent;

import ch.verno.db.entity.participant.ParentEntity;
import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SpringDataParentJpaRepository extends
        JpaRepository<ParentEntity, Long> {

  @Nonnull
  Optional<ParentEntity> findByIdAndTenant_Id(@Nonnull Long id,
                                              @Nonnull Long tenantId);

  @Nonnull
  List<ParentEntity> findAllByTenant_Id(@Nonnull Long tenantId);

  @Nonnull
  Optional<ParentEntity> findByTenant_IdAndFirstnameAndLastnameAndEmailAndPhone(
          Long tenantId,
          String firstname,
          String lastname,
          String email,
          String phone
  );
}
