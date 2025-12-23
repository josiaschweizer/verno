package ch.verno.db.address;

import ch.verno.server.repository.AddressRepository;
import ch.verno.server.entity.AddressEntity;
import jakarta.annotation.Nonnull;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.Optional;

public class JpaAddressRepository implements AddressRepository {

  @Nonnull
  private final SpringDataAddressJpaRepository springDataAddressJpaRepository;

  public JpaAddressRepository(@Nonnull final SpringDataAddressJpaRepository springDataAddressJpaRepository) {
    this.springDataAddressJpaRepository = springDataAddressJpaRepository;
  }


  @Override
  public @NonNull Optional<AddressEntity> findById(@NonNull final Long id) {
    return springDataAddressJpaRepository.findById(id);
  }

  @Override
  public @NonNull List<AddressEntity> findAll() {
    return springDataAddressJpaRepository.findAll();
  }

  @Override
  public void save(@NonNull final AddressEntity entity) {
    springDataAddressJpaRepository.save(entity);
  }
}
