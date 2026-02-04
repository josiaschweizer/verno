package ch.verno.db.jpa;

import ch.verno.db.entity.ParentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SpringDataParentJpaRepository extends JpaRepository<ParentEntity, Long> {

  Optional<ParentEntity> findByIdAndMandant_Id(Long id, Long mandantId);

  List<ParentEntity> findAllByMandant_Id(Long mandantId);

  Optional<ParentEntity> findByMandant_IdAndFirstnameAndLastnameAndEmailAndPhone(
          Long mandantId,
          String firstname,
          String lastname,
          String email,
          String phone
  );
}
