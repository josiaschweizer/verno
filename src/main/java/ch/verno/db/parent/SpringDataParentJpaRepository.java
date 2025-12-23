package ch.verno.db.parent;

import ch.verno.server.entity.ParentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataParentJpaRepository extends JpaRepository<ParentEntity, Long> {
}
