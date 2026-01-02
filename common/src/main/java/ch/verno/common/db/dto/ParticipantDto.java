package ch.verno.common.db.dto;

import ch.verno.common.base.components.entry.phonenumber.PhoneNumber;
import ch.verno.common.db.dto.base.BaseDto;
import ch.verno.common.util.Publ;
import ch.verno.common.util.phonenumber.PhoneNumberFormatter;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class ParticipantDto extends BaseDto {

  @Nonnull
  private String firstName;

  @Nonnull
  private String lastName;

  @Nullable
  private LocalDate birthdate;

  @Nonnull
  private GenderDto gender;

  @Nonnull
  private String email;

  @Nonnull
  private PhoneNumber phone;

  @Nonnull
  private String note;

  private boolean active;

  @Nonnull
  private List<CourseLevelDto> courseLevels;

  @Nonnull
  private List<CourseDto> courses;

  @Nonnull
  private AddressDto address;

  @Nonnull
  private ParentDto parentOne;

  @Nonnull
  private ParentDto parentTwo;

  public ParticipantDto() {
    setId(null);
    this.firstName = Publ.EMPTY_STRING;
    this.lastName = Publ.EMPTY_STRING;
    this.birthdate = null;
    this.gender = GenderDto.empty();
    this.email = Publ.EMPTY_STRING;
    this.phone = PhoneNumber.empty();
    this.note = Publ.EMPTY_STRING;
    this.active = true;
    this.courses = List.of();
    this.courseLevels = List.of();
    this.address = AddressDto.empty();
    this.parentOne = ParentDto.empty();
    this.parentTwo = ParentDto.empty();
  }

  public ParticipantDto(@Nullable final Long id,
                        @Nonnull final String firstName,
                        @Nonnull final String lastName,
                        @Nullable final LocalDate birthdate,
                        @Nonnull final GenderDto gender,
                        @Nonnull final String email,
                        @Nonnull final PhoneNumber phone,
                        @Nonnull final String note,
                        final boolean active,
                        @Nonnull final List<CourseDto> courses,
                        @Nonnull final List<CourseLevelDto> courseLevel,
                        @Nonnull final AddressDto address,
                        @Nonnull final ParentDto parentOne,
                        @Nonnull final ParentDto parentTwo) {
    setId(id);
    this.firstName = firstName;
    this.lastName = lastName;
    this.birthdate = birthdate;
    this.gender = gender;
    this.email = email;
    this.phone = phone;
    this.note = note;
    this.active = active;
    this.courses = courses;
    this.courseLevels = courseLevel;
    this.address = address;
    this.parentOne = parentOne;
    this.parentTwo = parentTwo;
  }

  public boolean isEmpty() {
    return getId() != null
            && getId() == 0L
            && firstName.isEmpty()
            && lastName.isEmpty()
            && (birthdate == null)
            && gender.isEmpty()
            && email.isEmpty()
            && phone.isEmpty()
            && note.isEmpty()
            && courses.isEmpty()
            && courseLevels.isEmpty()
            && address.isEmpty()
            && parentOne.isEmpty()
            && parentTwo.isEmpty();
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
  public String getFullName() {
    return firstName + Publ.SPACE + lastName;
  }

  @Nullable
  public LocalDate getBirthdate() {
    return birthdate;
  }

  public void setBirthdate(@Nullable final LocalDate birthdate) {
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
  public PhoneNumber getPhone() {
    return phone;
  }

  public void setPhone(@Nonnull final PhoneNumber phone) {
    this.phone = Objects.requireNonNullElseGet(phone, PhoneNumber::empty);
  }

  @Nonnull
  public String getPhoneString() {
    return PhoneNumberFormatter.formatPhoneNumber(phone);
  }

  @Nonnull
  public String getNote() {
    return note;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(final boolean active) {
    this.active = active;
  }

  public void setNote(@Nonnull final String note) {
    this.note = note;
  }

  @Nonnull
  public List<CourseLevelDto> getCourseLevels() {
    return courseLevels;
  }

  public void setCourseLevels(@Nonnull final List<CourseLevelDto> courseLevels) {
    this.courseLevels = courseLevels;
  }

  @Nonnull
  public List<CourseDto> getCourses() {
    return courses;
  }

  public void setCourses(@Nonnull final List<CourseDto> courses) {
    this.courses = courses;
  }

  public void addCourse(@Nonnull final CourseDto course) {
    if (!this.courses.contains(course)) {
      this.courses.add(course);
    }
  }

  @Nonnull
  public AddressDto getAddress() {
    return address;
  }

  public void setAddress(@Nonnull final AddressDto address) {
    this.address = address;
  }

  @Nonnull
  public ParentDto getParentOne() {
    return parentOne;
  }

  public void setParentOne(@Nonnull final ParentDto parentOne) {
    this.parentOne = parentOne;
  }

  @Nonnull
  public ParentDto getParentTwo() {
    return parentTwo;
  }

  public void setParentTwo(@Nonnull final ParentDto parentTwo) {
    this.parentTwo = parentTwo;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof ParticipantDto other)) return false;
    return getId() != null && getId().equals(other.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId());
  }
}