package ch.verno.common.db.dto;

import ch.verno.common.base.components.entry.phonenumber.PhoneNumber;
import ch.verno.common.util.Publ;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public class InstructorDto {

  @Nullable
  private Long id;

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

  public InstructorDto() {
    this.id = 0L;
    this.firstName = Publ.EMPTY_STRING;
    this.lastName = Publ.EMPTY_STRING;
    this.email = Publ.EMPTY_STRING;
    this.phone = PhoneNumber.empty();
    this.gender = GenderDto.empty();
    this.address = AddressDto.empty();
  }

  public InstructorDto(@Nullable final Long id,
                       @Nonnull final String firstName,
                       @Nonnull final String lastName,
                       @Nonnull final String email,
                       @Nonnull final PhoneNumber phone,
                       @Nonnull final GenderDto gender,
                       @Nonnull final AddressDto address) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.phone = phone;
    this.gender = gender;
    this.address = address;
  }

  @Nullable
  public Long getId() {
    return id;
  }

  public void setId(@Nullable final Long id) {
    this.id = id;
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
    this.phone = phone;
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

  public boolean isEmpty() {
    return this.id != null
            && this.id == 0L
            && this.firstName.isEmpty()
            && this.lastName.isEmpty()
            && this.email.isEmpty()
            && this.phone.isEmpty();
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
  public String genderAsString() {
    return gender.description();
  }
}