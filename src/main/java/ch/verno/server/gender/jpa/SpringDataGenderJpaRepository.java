package ch.verno.server.gender.jpa;

import ch.verno.server.gender.entity.GenderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataGenderJpaRepository extends JpaRepository<GenderEntity, Long> {
}
