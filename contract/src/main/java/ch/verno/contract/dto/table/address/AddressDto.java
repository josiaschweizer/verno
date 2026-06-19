package ch.verno.contract.dto.table.address;

import ch.verno.contract.dto.table.base.BaseDto;
import ch.verno.lib.Publ;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.Objects;

public class AddressDto extends BaseDto {

  @Nonnull private String street;
  @Nonnull private String houseNumber;
  @Nonnull private String zipCode;
  @Nonnull private String city;
  @Nonnull private String country;

  private AddressDto() {
    super.setId(null);
    this.street = Publ.EMPTY_STRING;
    this.houseNumber = Publ.EMPTY_STRING;
    this.zipCode = Publ.EMPTY_STRING;
    this.city = Publ.EMPTY_STRING;
    this.country = Publ.EMPTY_STRING;
  }

  public AddressDto(@Nonnull final String street,
                    @Nonnull final String houseNumber,
                    @Nonnull final String zipCode,
                    @Nonnull final String city,
                    @Nonnull final String country) {
    setId(null);
    this.street = street;
    this.houseNumber = houseNumber;
    this.zipCode = zipCode;
    this.city = city;
    this.country = country;
  }

  public AddressDto(@Nullable final Long id,
                    @Nonnull final String street,
                    @Nonnull final String houseNumber,
                    @Nonnull final String zipCode,
                    @Nonnull final String city,
                    @Nonnull final String country) {
    super.setId(id);
    this.street = street;
    this.houseNumber = houseNumber;
    this.zipCode = zipCode;
    this.city = city;
    this.country = country;
  }

  @Nonnull
  public static AddressDto empty() {
    return new AddressDto();
  }

  @Nonnull
  public static AddressDto ref(@Nonnull final Long id) {
    final var dto = new AddressDto();
    dto.setId(id);
    return dto;
  }

  public boolean isEmpty() {
    return getId() != null
            && getId() == 0L
            && street.isEmpty()
            && houseNumber.isEmpty()
            && zipCode.isEmpty()
            && city.isEmpty()
            && country.isEmpty();
  }

  @Nonnull
  public String getStreet() {
    return street;
  }

  public void setStreet(@Nonnull final String street) {
    this.street = street;
  }

  @Nonnull
  public String getHouseNumber() {
    return houseNumber;
  }

  public void setHouseNumber(@Nonnull final String houseNumber) {
    this.houseNumber = houseNumber;
  }

  @Nonnull
  public String getZipCode() {
    return zipCode;
  }

  public void setZipCode(@Nonnull final String zipCode) {
    this.zipCode = zipCode;
  }

  @Nonnull
  public String getCity() {
    return city;
  }

  public void setCity(@Nonnull final String city) {
    this.city = city;
  }

  @Nonnull
  public String getCountry() {
    return country;
  }

  public void setCountry(@Nonnull final String country) {
    this.country = country;
  }

  @Nonnull
  public String getFullAddressAsString() {
    StringBuilder fullAddress = new StringBuilder();
    if (!street.isEmpty()) {
      fullAddress.append(street);
      if (!houseNumber.isEmpty()) {
        fullAddress.append(" ").append(houseNumber);
      }
      fullAddress.append(", ");
    }
    if (!zipCode.isEmpty()) {
      fullAddress.append(zipCode).append(" ");
    }
    if (!city.isEmpty()) {
      fullAddress.append(city);
    }
    if (!country.isEmpty()) {
      if (!fullAddress.isEmpty()) {
        fullAddress.append(", ");
      }
      fullAddress.append(country);
    }
    return fullAddress.toString().trim();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof AddressDto other)) return false;
    return getId() != null && getId().equals(other.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId());
  }
}