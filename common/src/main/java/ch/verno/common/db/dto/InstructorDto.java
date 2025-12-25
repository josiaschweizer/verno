package ch.verno.common.db.dto;

import ch.verno.common.base.components.entry.phonenumber.PhoneNumber;
import ch.verno.common.util.Publ;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public record InstructorDto(@Nullable Long id,
                            @Nonnull String firstName,
                            @Nonnull String lastName,
                            @Nonnull String email,
                            @Nonnull PhoneNumber phone,
                            @Nonnull GenderDto gender,
                            @Nonnull AddressDto address) {

  public static InstructorDto empty() {
    return new InstructorDto(
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
        && this.phone().isEmpty();
  }

  @Nonnull
  public String displayName() {
    return (firstName + " " + lastName).trim();
  }

  @Nonnull
  public String phoneAsString() {
    return phone.isEmpty() ? Publ.EMPTY_STRING : phone.toString();
  }

  @Nonnull
  public String genderAsString(){
    return gender.description();
  }
}