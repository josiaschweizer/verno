package ch.verno.common.db.dto;

import ch.verno.common.base.components.entry.phonenumber.PhoneNumber;
import ch.verno.common.util.Publ;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.time.LocalDate;

public class ParticipantDto {

  @Nullable
  Long id;
  @Nonnull
  String firstName;
  @Nonnull
  String lastName;
  @Nonnull
  LocalDate birthdate;
  @Nonnull
  GenderDto gender;
  @Nonnull
  String email;
  @Nonnull
  PhoneNumber phone;
  @Nonnull
  CourseLevelDto courseLevel;
  @Nonnull
  CourseDto course;
  @Nonnull
  AddressDto address;
  @Nonnull
  ParentDto parentOne;
  @Nonnull
  ParentDto parentTwo;

  public ParticipantDto(@Nullable final Long id,
                        @Nonnull final String firstName,
                        @Nonnull final String lastName,
                        @Nonnull final LocalDate birthdate,
                        @Nonnull final GenderDto gender,
                        @Nonnull final String email,
                        @Nonnull final PhoneNumber phone,
                        @Nonnull final CourseLevelDto courseLevel,
                        @Nonnull final CourseDto course,
                        @Nonnull final AddressDto address,
                        @Nonnull final ParentDto parentOne,
                        @Nonnull final ParentDto parentTwo) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.birthdate = birthdate;
    this.gender = gender;
    this.email = email;
    this.phone = phone;
    this.courseLevel = courseLevel;
    this.course = course;
    this.address = address;
    this.parentOne = parentOne;
    this.parentTwo = parentTwo;
  }

  public ParticipantDto() {
    this.id = 0L;
    this.firstName = Publ.EMPTY_STRING;
    this.lastName = Publ.EMPTY_STRING;
    this.birthdate = LocalDate.now();
    this.gender = GenderDto.empty();
    this.email = Publ.EMPTY_STRING;
    this.phone = PhoneNumber.empty();
    this.courseLevel = CourseLevelDto.empty();
    this.course = CourseDto.empty();
    this.address = AddressDto.empty();
    this.parentOne = ParentDto.empty();
    this.parentTwo = ParentDto.empty();

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
  public PhoneNumber getPhone() {
    return phone;
  }

  public void setPhone(@Nonnull final PhoneNumber phone) {
    this.phone = phone;
  }

  @Nonnull
  public CourseLevelDto getCourseLevel() {
    return courseLevel;
  }

  public void setCourseLevel(@Nonnull final CourseLevelDto courseLevel) {
    this.courseLevel = courseLevel;
  }

  @Nonnull
  public CourseDto getCourse() {
    return course;
  }

  public void setCourse(@Nonnull final CourseDto course) {
    this.course = course;
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
}