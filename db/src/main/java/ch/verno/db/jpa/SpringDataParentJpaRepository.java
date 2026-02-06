package ch.verno.db.jpa;

import ch.verno.db.entity.ParentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SpringDataParentJpaRepository extends JpaRepository<ParentEntity, Long> {

  Optional<ParentEntity> findByIdAndTenant_Id(Long id, Long tenantId);

  List<ParentEntity> findAllByTenant_Id(Long tenantId);

  Optional<ParentEntity> findByTenant_IdAndFirstnameAndLastnameAndEmailAndPhone(
          Long tenantId,
          String firstname,
          String lastname,
          String email,
          String phone
  );
}
