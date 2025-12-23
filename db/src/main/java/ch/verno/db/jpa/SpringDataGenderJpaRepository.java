package ch.verno.db.jpa;

import ch.verno.db.entity.GenderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataGenderJpaRepository extends JpaRepository<GenderEntity, Long> {
}
