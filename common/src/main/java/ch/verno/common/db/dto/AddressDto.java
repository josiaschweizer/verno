package ch.verno.common.db.dto;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public record AddressDto(
    @Nullable Long id,
    @Nonnull String street,
    @Nonnull String houseNumber,
    @Nonnull String zipCode,
    @Nonnull String city,
    @Nonnull String country
) {

  @Nonnull
  public static AddressDto empty() {
    return new AddressDto(
        0L,
        "",
        "",
        "",
        "",
        ""
    );
  }

  public boolean isEmpty() {
    return this.id() != null
        && this.id() == 0L
        && this.street().isEmpty()
        && this.houseNumber().isEmpty()
        && this.zipCode().isEmpty()
        && this.city().isEmpty()
        && this.country().isEmpty();
  }

}