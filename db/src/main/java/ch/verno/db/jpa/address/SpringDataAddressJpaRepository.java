package ch.verno.db.jpa.address;

import ch.verno.db.entity.address.AddressEntity;
import ch.verno.db.jpa.base.AbstractEntityJpaRepository;
import jakarta.annotation.Nonnull;

import java.util.Optional;

public interface SpringDataAddressJpaRepository extends
        AbstractEntityJpaRepository<AddressEntity, Long> {

  @Nonnull
  Optional<AddressEntity> findByStreetAndHouseNumberAndZipCodeAndCityAndCountry(
          @Nonnull String street,
          @Nonnull String houseNumber,
          @Nonnull String zipCode,
          @Nonnull String city,
          @Nonnull String country
  );
}
