package ch.verno.server.repository;

import ch.verno.db.entity.AddressEntity;
import ch.verno.db.jpa.SpringDataAddressJpaRepository;
import jakarta.annotation.Nonnull;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.Optional;

public class AddressRepository {

  @Nonnull
  private final SpringDataAddressJpaRepository springDataAddressJpaRepository;

  public AddressRepository(@Nonnull final SpringDataAddressJpaRepository springDataAddressJpaRepository) {
    this.springDataAddressJpaRepository = springDataAddressJpaRepository;
  }


  @Nonnull
  public Optional<AddressEntity> findById(@NonNull final Long id) {
    return springDataAddressJpaRepository.findById(id);
  }

  @Nonnull
  public List<AddressEntity> findAll() {
    return springDataAddressJpaRepository.findAll();
  }

  public void save(@Nonnull final AddressEntity entity) {
    springDataAddressJpaRepository.save(entity);
  }
}
