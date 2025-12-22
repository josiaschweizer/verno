package ch.verno.ui.verno.participant.dto;

import ch.verno.common.base.components.entry.phonenumber.PhoneNumber;
import ch.verno.common.util.Publ;
import jakarta.annotation.Nonnull;

import java.time.LocalDate;

public class ParticipantDto {
  @Nonnull
  private Long id;
  @Nonnull
  private String firstName;
  @Nonnull
  private String lastName;
  @Nonnull
  private LocalDate birthdate;
  @Nonnull
  private GenderDto gender;
  @Nonnull
  private String email;
  @Nonnull
  private PhoneNumber phoneNumber;

  public ParticipantDto() {
    this.id = 0L;
    this.firstName = Publ.EMPTY_STRING;
    this.lastName = Publ.EMPTY_STRING;
    this.birthdate = LocalDate.now();
    this.gender = new GenderDto(0L, Publ.EMPTY_STRING);
    this.email = Publ.EMPTY_STRING;
    this.phoneNumber = PhoneNumber.empty();
  }

  public ParticipantDto(@Nonnull final Long id,
                        @Nonnull final String firstName,
                        @Nonnull final String lastName,
                        @Nonnull final LocalDate birthdate,
                        @Nonnull final GenderDto gender,
                        @Nonnull final String email,
                        @Nonnull final PhoneNumber phoneNumber) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.birthdate = birthdate;
    this.gender = gender;
    this.email = email;
    this.phoneNumber = phoneNumber;
  }


  @Nonnull
  public Long getId() {
    return id;
  }

  public void setId(@Nonnull final Long id) {
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
  public LocalDate getBirthdate() {
    return birthdate;
  }

  public void setBirthdate(@Nonnull final LocalDate birthdate) {
    this.birthdate = birthdate;
  }

  @Nonnull
  public GenderDto getGender() {
    return gender;
  }

  public void setGender(@Nonnull final GenderDto gender) {
    this.gender = gender;
  }

  @Nonnull
  public String getEmail() {
    return email;
  }

  public void setEmail(@Nonnull final String email) {
    this.email = email;
  }

  @Nonnull
  public PhoneNumber getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(@Nonnull final PhoneNumber phoneNumber) {
    this.phoneNumber = phoneNumber;
  }
}
