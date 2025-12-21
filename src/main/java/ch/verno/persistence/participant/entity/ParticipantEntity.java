package ch.verno.persistence.participant.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Table(name = "participant", schema = "public")
public class ParticipantEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "created_at", nullable = false)
  private OffsetDateTime createdAt;

  @Column(name = "firstname")
  private String firstname;

  @Column(name = "lastname")
  private String lastname;

  @Column(name = "birthdate")
  private LocalDate birthdate;

  @Column(name = "gender")
  private Long genderId;

  @Column(name = "course_level")
  private Long courseLevelId;

  @Column(name = "course")
  private Long courseId;

  @Column(name = "address")
  private Long addressId;

  @Column(name = "email")
  private String email;

  @Column(name = "phone")
  private String phone;

  @Column(name = "parent_one")
  private Long parentOneId;

  @Column(name = "parent_two")
  private Long parentTwoId;

  protected ParticipantEntity() {
    // JPA
  }

  public Long getId() {
    return id;
  }

  public void setId(final Long id) {
    this.id = id;
  }

  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public String getFirstname() {
    return firstname;
  }

  public void setFirstname(String firstname) {
    this.firstname = firstname;
  }

  public String getLastname() {
    return lastname;
  }

  public void setLastname(String lastname) {
    this.lastname = lastname;
  }

  public LocalDate getBirthdate() {
    return birthdate;
  }

  public void setBirthdate(LocalDate birthdate) {
    this.birthdate = birthdate;
  }

  public Long getGenderId() {
    return genderId;
  }

  public void setGenderId(Long genderId) {
    this.genderId = genderId;
  }

  public Long getCourseLevelId() {
    return courseLevelId;
  }

  public void setCourseLevelId(Long courseLevelId) {
    this.courseLevelId = courseLevelId;
  }

  public Long getCourseId() {
    return courseId;
  }

  public void setCourseId(Long courseId) {
    this.courseId = courseId;
  }

  public Long getAddressId() {
    return addressId;
  }

  public void setAddressId(Long addressId) {
    this.addressId = addressId;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public Long getParentOneId() {
    return parentOneId;
  }

  public void setParentOneId(Long parentOneId) {
    this.parentOneId = parentOneId;
  }

  public Long getParentTwoId() {
    return parentTwoId;
  }

  public void setParentTwoId(Long parentTwoId) {
    this.parentTwoId = parentTwoId;
  }
}