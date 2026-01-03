package ch.verno.ui.verno.dashboard.report;

import jakarta.annotation.Nonnull;

public class ReportParticipant {

  @Nonnull
  private String firstName;
  @Nonnull
  private String lastName;
  @Nonnull
  private String email;

  public ReportParticipant(@Nonnull final String firstName,
                           @Nonnull final String lastName,
                           @Nonnull final String email) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
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
}
