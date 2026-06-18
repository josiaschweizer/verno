package ch.verno.server.repository.address;

import ch.verno.db.entity.address.AddressEntity;
import ch.verno.db.jpa.address.SpringDataAddressJpaRepository;
import ch.verno.server.repository.base.AbstractEntityRepository;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class AddressRepository extends AbstractEntityRepository<
        AddressEntity,
        Long,
        SpringDataAddressJpaRepository> {

  public AddressRepository(@Nonnull final SpringDataAddressJpaRepository jpaRepository) {
    super(jpaRepository);
  }

  @Nonnull
  public Optional<AddressEntity> findByFields(@Nonnull final String street,
                                              @Nonnull final String houseNumber,
                                              @Nonnull final String zipCode,
                                              @Nonnull final String city,
                                              @Nonnull final String country) {
    return getRepository().findByStreetAndHouseNumberAndZipCodeAndCityAndCountry(
            street,
            houseNumber,
            zipCode,
            city,
            country
    );
  }
}