package ch.verno.db.gender;

import ch.verno.server.entity.GenderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataGenderJpaRepository extends JpaRepository<GenderEntity, Long> {
}
