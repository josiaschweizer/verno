package ch.verno.db.entity;

import ch.verno.db.entity.tenant.TenantEntity;
import ch.verno.db.entity.tenant.TenantEntityListener;
import ch.verno.db.entity.tenant.TenantScopedEntity;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "participant", schema = "public")
@EntityListeners(TenantEntityListener.class)
public class ParticipantEntity extends TenantScopedEntity {

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

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
          name = "participant_course_level",
          joinColumns = @JoinColumn(name = "participant_id"),
          inverseJoinColumns = @JoinColumn(name = "course_level_id")
  )
  private List<CourseLevelEntity> courseLevels = new ArrayList<>();

  @ManyToMany
  @JoinTable(
          name = "participant_course",
          joinColumns = @JoinColumn(name = "participant_id"),
          inverseJoinColumns = @JoinColumn(name = "course_id")
  )
  private List<CourseEntity> courses = new ArrayList<>();

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "address")
  private AddressEntity address;

  @Column(name = "email")
  private String email;

  @Column(name = "phone")
  private String phone;

  @Column(name = "note", columnDefinition = "text")
  private String note;

  @Column(name = "active", nullable = false)
  private boolean active = true;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_one")
  private ParentEntity parentOne;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_two")
  private ParentEntity parentTwo;

  protected ParticipantEntity() {
    // JPA
  }

  public ParticipantEntity(@Nonnull final TenantEntity tenant,
                           @Nonnull final String firstname,
                           @Nonnull final String lastname,
                           @Nonnull final LocalDate birthdate,
                           @Nonnull final String email,
                           @Nonnull final String phone,
                           @Nonnull final String note,
                           final boolean active) {
    setTenant(tenant);
    this.firstname = firstname;
    this.lastname = lastname;
    this.birthdate = birthdate;
    this.email = email;
    this.phone = phone;
    this.note = note;
    this.active = active;
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

  public List<CourseLevelEntity> getCourseLevels() {
    return courseLevels;
  }

  public void setCourseLevels(final List<CourseLevelEntity> courseLevels) {
    this.courseLevels.clear();
    if (courseLevels != null) {
      this.courseLevels.addAll(courseLevels);
    }
  }

  public List<CourseEntity> getCourses() {
    return courses;
  }

  public void setCourses(final List<CourseEntity> courses) {
    this.courses.clear();
    if (courses != null) {
      this.courses.addAll(courses);
    }
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

  public boolean isActive() {
    return active;
  }

  public void setActive(final boolean active) {
    this.active = active;
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