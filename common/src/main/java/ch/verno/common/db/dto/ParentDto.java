package ch.verno.common.db.dto;

import ch.verno.common.base.components.entry.phonenumber.PhoneNumber;
import ch.verno.common.db.dto.base.BaseDto;
import ch.verno.common.util.Publ;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.Objects;

public class ParentDto extends BaseDto {

  @Nonnull
  private String firstName;

  @Nonnull
  private String lastName;

  @Nonnull
  private String email;

  @Nonnull
  private PhoneNumber phone;

  @Nonnull
  private GenderDto gender;

  @Nonnull
  private AddressDto address;

  public ParentDto() {
    setId(null);
    this.firstName = Publ.EMPTY_STRING;
    this.lastName = Publ.EMPTY_STRING;
    this.email = Publ.EMPTY_STRING;
    this.phone = PhoneNumber.empty();
    this.gender = GenderDto.empty();
    this.address = AddressDto.empty();
  }

  public ParentDto(@Nullable final Long id,
                   @Nonnull final String firstName,
                   @Nonnull final String lastName,
                   @Nonnull final String email,
                   @Nonnull final PhoneNumber phone,
                   @Nonnull final GenderDto gender,
                   @Nonnull final AddressDto address) {
    setId(id);
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.phone = phone;
    this.gender = gender;
    this.address = address;
  }

  @Nonnull
  public static ParentDto empty() {
    return new ParentDto();
  }

  public boolean isEmpty() {
    return getId() != null
            && getId() == 0L
            && firstName.isEmpty()
            && lastName.isEmpty()
            && email.isEmpty()
            && phone.isEmpty();
  }

  @Nonnull
  public String displayName() {
    return (firstName + Publ.SPACE + lastName).trim();
  }

  @Nonnull
  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(@Nonnull final String firstName) {
    this.firstName = firstName;
  }

  @Nonnull
  public String getLastName() {
    return lastName;
  }

  public void setLastName(@Nonnull final String lastName) {
    this.lastName = lastName;
  }

  @Nonnull
  public String getEmail() {
    return email;
  }

  public void setEmail(@Nonnull final String email) {
    this.email = email;
  }

  @Nonnull
  public PhoneNumber getPhone() {
    return phone;
  }

  public void setPhone(@Nonnull final PhoneNumber phone) {
    this.phone = Objects.requireNonNullElseGet(phone, PhoneNumber::empty);
  }

  @Nonnull
  public GenderDto getGender() {
    return gender;
  }

  public void setGender(@Nonnull final GenderDto gender) {
    this.gender = gender;
  }

  @Nonnull
  public AddressDto getAddress() {
    return address;
  }

  public void setAddress(@Nonnull final AddressDto address) {
    this.address = address;
  }
}