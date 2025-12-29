package ch.verno.server.repository;

import ch.verno.db.entity.AddressEntity;
import ch.verno.db.jpa.SpringDataAddressJpaRepository;
import jakarta.annotation.Nonnull;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class AddressRepository {

  @Nonnull
  private final SpringDataAddressJpaRepository springDataAddressJpaRepository;

  public AddressRepository(@Nonnull final SpringDataAddressJpaRepository springDataAddressJpaRepository) {
    this.springDataAddressJpaRepository = springDataAddressJpaRepository;
  }


  @Nonnull
  public Optional<AddressEntity> findById(@Nonnull final Long id) {
    return springDataAddressJpaRepository.findById(id);
  }

  @Nonnull
  public List<AddressEntity> findAll() {
    return springDataAddressJpaRepository.findAll();
  }

  @Nonnull
  public AddressEntity save(@Nonnull final AddressEntity entity) {
    return springDataAddressJpaRepository.save(entity);
  }
}
