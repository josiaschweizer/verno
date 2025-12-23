package ch.verno.db.jpa;

import ch.verno.db.entity.ParentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataParentJpaRepository extends JpaRepository<ParentEntity, Long> {
}
