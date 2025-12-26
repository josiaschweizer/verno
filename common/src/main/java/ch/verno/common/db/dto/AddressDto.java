package ch.verno.common.db.dto;

import ch.verno.common.db.dto.base.BaseDto;
import ch.verno.common.util.Publ;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public class AddressDto extends BaseDto {

  @Nonnull
  private String street;

  @Nonnull
  private String houseNumber;

  @Nonnull
  private String zipCode;

  @Nonnull
  private String city;

  @Nonnull
  private String country;

  public AddressDto() {
    super.setId(0L);
    this.street = Publ.EMPTY_STRING;
    this.houseNumber = Publ.EMPTY_STRING;
    this.zipCode = Publ.EMPTY_STRING;
    this.city = Publ.EMPTY_STRING;
    this.country = Publ.EMPTY_STRING;
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
}