package ch.verno.ui.verno.participant;

import ch.verno.util.Publ;
import jakarta.annotation.Nonnull;

public class ParticipantDto {
  @Nonnull
  private Long id;
  @Nonnull
  private String firstName;
  @Nonnull
  private String lastName;
  private int age;
  @Nonnull
  private String email;
  @Nonnull
  private String phone;

  public ParticipantDto() {
    this.id = 0L;
    this.firstName = Publ.EMPTY_STRING;
    this.lastName = Publ.EMPTY_STRING;
    this.age = 0;
    this.email = Publ.EMPTY_STRING;
    this.phone = Publ.EMPTY_STRING;
  }

  public ParticipantDto(@Nonnull final Long id,
                        @Nonnull final String firstName,
                        @Nonnull final String lastName,
                        final int age,
                        @Nonnull final String email,
                        @Nonnull final String phone) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.age = age;
    this.email = email;
    this.phone = phone;
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

  public int getAge() {
    return age;
  }

  public void setAge(final int age) {
    this.age = age;
  }

  @Nonnull
  public String getEmail() {
    return email;
  }

  public void setEmail(@Nonnull final String email) {
    this.email = email;
  }

  @Nonnull
  public String getPhone() {
    return phone;
  }

  public void setPhone(@Nonnull final String phone) {
    this.phone = phone;
  }
}
