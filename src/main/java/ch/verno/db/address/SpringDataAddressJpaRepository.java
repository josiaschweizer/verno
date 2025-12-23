package ch.verno.db.address;

import ch.verno.server.entity.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataAddressJpaRepository extends JpaRepository<AddressEntity, Long> {
}
