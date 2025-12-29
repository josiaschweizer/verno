package ch.verno.db.entity;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Table(name = "participant", schema = "public")
public class ParticipantEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Nonnull
  @Column(name = "created_at", nullable = false)
  private OffsetDateTime createdAt = OffsetDateTime.now();

  @Column(name = "firstname")
  private String firstname;

  @Column(name = "lastname")
  private String lastname;

  @Column(name = "birthdate")
  private LocalDate birthdate;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "gender")
  private GenderEntity gender;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "course_level")
  private CourseLevelEntity courseLevel;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "course")
  private CourseEntity course;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "address")
  private AddressEntity address;

  @Column(name = "email")
  private String email;

  @Column(name = "phone")
  private String phone;

  @Lob
  @Column(name = "note")
  private String note;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_one")
  private ParentEntity parentOne;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_two")
  private ParentEntity parentTwo;

  protected ParticipantEntity() {
    // JPA
  }

  public ParticipantEntity(@Nonnull final String firstname,
                           @Nonnull final String lastname,
                           @Nonnull final LocalDate birthdate,
                           @Nonnull final String email,
                           @Nonnull final String phone,
                           @Nonnull final String note) {
    this.firstname = firstname;
    this.lastname = lastname;
    this.birthdate = birthdate;
    this.email = email;
    this.phone = phone;
    this.note = note;
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

  public String getFirstname() {
    return firstname;
  }

  public void setFirstname(final String firstname) {
    this.firstname = firstname;
  }

  public String getLastname() {
    return lastname;
  }

  public void setLastname(final String lastname) {
    this.lastname = lastname;
  }

  public LocalDate getBirthdate() {
    return birthdate;
  }

  public void setBirthdate(final LocalDate birthdate) {
    this.birthdate = birthdate;
  }

  public GenderEntity getGender() {
    return gender;
  }

  public void setGender(final GenderEntity gender) {
    this.gender = gender;
  }

  public CourseLevelEntity getCourseLevel() {
    return courseLevel;
  }

  public void setCourseLevel(final CourseLevelEntity courseLevel) {
    this.courseLevel = courseLevel;
  }

  public CourseEntity getCourse() {
    return course;
  }

  public void setCourse(final CourseEntity course) {
    this.course = course;
  }

  public AddressEntity getAddress() {
    return address;
  }

  public void setAddress(final AddressEntity address) {
    this.address = address;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(final String email) {
    this.email = email;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(final String phone) {
    this.phone = phone;
  }


  public String getNote() {
    return note;
  }

  public void setNote(final String note) {
    this.note = note;
  }

  public ParentEntity getParentOne() {
    return parentOne;
  }

  public void setParentOne(final ParentEntity parentOne) {
    this.parentOne = parentOne;
  }

  public ParentEntity getParentTwo() {
    return parentTwo;
  }

  public void setParentTwo(final ParentEntity parentTwo) {
    this.parentTwo = parentTwo;
  }
}