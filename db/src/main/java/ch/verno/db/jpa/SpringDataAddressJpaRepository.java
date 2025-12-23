package ch.verno.db.jpa;

import ch.verno.db.entity.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataAddressJpaRepository extends JpaRepository<AddressEntity, Long> {
}
