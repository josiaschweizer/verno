package ch.verno.contract.dto.table.participant;

import ch.verno.common.dto.ui.phonenumber.PhoneNumber;
import ch.verno.common.lib.phonenumber.PhoneNumberFormatter;
import ch.verno.contract.dto.table.address.AddressDto;
import ch.verno.contract.dto.table.base.BaseDto;
import ch.verno.contract.dto.table.course.CourseDto;
import ch.verno.contract.dto.table.course.CourseLevelDto;
import ch.verno.contract.dto.table.gender.GenderDto;
import ch.verno.lib.Publ;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ParticipantDto extends BaseDto {

  @Nonnull private String firstName;
  @Nonnull private String lastName;
  @Nullable private LocalDate birthdate;

  @Nonnull private GenderDto gender;
  @Nonnull private String email;
  @Nonnull private PhoneNumber phone;
  @Nonnull private String note;
  private boolean active;
  @Nonnull private List<CourseLevelDto> courseLevels;
  @Nonnull private List<CourseDto> courses;
  @Nonnull private AddressDto address;

  @Nonnull private ParentDto parentOne;
  @Nonnull private ParentDto parentTwo;
  @Nonnull private List<ParticipantDto> siblings;

  private ParticipantDto() {
    setId(null);
    this.firstName = Publ.EMPTY_STRING;
    this.lastName = Publ.EMPTY_STRING;
    this.birthdate = null;
    this.gender = GenderDto.empty();
    this.email = Publ.EMPTY_STRING;
    this.phone = PhoneNumber.empty();
    this.note = Publ.EMPTY_STRING;
    this.active = true;
    this.courses = new ArrayList<>();
    this.courseLevels = new ArrayList<>();
    this.address = AddressDto.empty();
    this.parentOne = ParentDto.empty();
    this.parentTwo = ParentDto.empty();
    this.siblings = new ArrayList<>();
  }

  public ParticipantDto(@Nonnull final String firstName,
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
                        @Nonnull final ParentDto parentTwo,
                        @Nonnull final List<ParticipantDto> siblings) {
    this(null, firstName, lastName, birthdate, gender, email, phone, note, active, courses, courseLevel, address, parentOne, parentTwo, siblings);
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
                        @Nonnull final ParentDto parentTwo,
                        @Nonnull final List<ParticipantDto> siblings) {
    setId(id);
    this.firstName = firstName;
    this.lastName = lastName;
    this.birthdate = birthdate;
    this.gender = gender;
    this.email = email;
    this.phone = phone;
    this.note = note;
    this.active = active;
    this.courses = new ArrayList<>(courses);
    this.courseLevels = new ArrayList<>(courseLevel);
    this.address = address;
    this.parentOne = parentOne;
    this.parentTwo = parentTwo;
    this.siblings = siblings;
  }

  @Nonnull
  public static ParticipantDto empty() {
    return new ParticipantDto();
  }

  @Nonnull
  public static ParticipantDto ref(@Nonnull final Long id) {
    final var dto = ParticipantDto.empty();
    dto.setId(id);
    return dto;
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
            && parentTwo.isEmpty()
            && siblings.isEmpty();
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
  public String getDisplayName() {
    if (firstName.isEmpty() && lastName.isEmpty()) {
      return Publ.EMPTY_STRING;
    } else if (firstName.isEmpty()) {
      return lastName;
    } else if (lastName.isEmpty()) {
      return firstName;
    } else {
      return firstName + Publ.SPACE + lastName;
    }
  }

  @Nonnull
  public String getFullName() {
    return firstName + Publ.SPACE + lastName;
  }

  @Nullable
  public LocalDate getBirthdate() {
    return birthdate;
  }

  @Nullable
  public Integer getAgeFromBirthday() {
    if (birthdate == null || birthdate.equals(LocalDate.now())) {
      return null;
    }
    return Period.between(birthdate, LocalDate.now()).getYears();
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

  public String getGenderAsString() {
    return gender.getName();
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
    this.courseLevels = new ArrayList<>(courseLevels);
  }

  public void addCourseLevel(@Nonnull final CourseLevelDto courseLevel) {
    this.courseLevels.add(courseLevel);
  }

  @Nonnull
  public String getCourseLevelsAsString() {
    final var names = new ArrayList<String>();

    for (final var level : courseLevels) {
      names.add(level.getName());
    }

    return String.join(Publ.COMMA + Publ.SPACE, names);
  }

  @Nonnull
  public List<CourseDto> getCourses() {
    return courses;
  }

  public void setCourses(@Nonnull final List<CourseDto> courses) {
    this.courses = new ArrayList<>(courses);
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

  @Nonnull
  public List<ParticipantDto> getSiblings() {
    return siblings;
  }

  public void setSiblings(@Nonnull final List<ParticipantDto> siblings) {
    this.siblings = siblings;
  }

  @Override
  public boolean equals(@Nullable final Object o) {
    if (this == o) return true;
    if (!(o instanceof ParticipantDto other)) return false;
    return getId() != null && getId().equals(other.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId());
  }
}