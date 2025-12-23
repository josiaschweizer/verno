package ch.verno.common.db.dto;

import ch.verno.common.base.components.entry.phonenumber.PhoneNumber;
import ch.verno.common.util.Publ;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public record ParentDto(
    @Nullable Long id,
    @Nonnull String firstName,
    @Nonnull String lastName,
    @Nonnull String email,
    @Nonnull PhoneNumber phoneNumber,
    @Nonnull GenderDto gender,
    @Nonnull AddressDto address
) {

  public static ParentDto empty() {
    return new ParentDto(
        0L,
        Publ.EMPTY_STRING,
        Publ.EMPTY_STRING,
        Publ.EMPTY_STRING,
        PhoneNumber.empty(),
        GenderDto.empty(),
        AddressDto.empty()
    );
  }

  public boolean isEmpty() {
    return this.id() != null
        && this.id() == 0L
        && this.firstName().isEmpty()
        && this.lastName().isEmpty()
        && this.email().isEmpty()
        && this.phoneNumber().isEmpty();
  }

  public String displayName() {
    return (firstName + Publ.SPACE + lastName).trim();
  }
}