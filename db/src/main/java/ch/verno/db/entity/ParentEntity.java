package ch.verno.db.entity;

import ch.verno.db.entity.mandant.MandantEntity;
import ch.verno.db.entity.mandant.MandantEntityListener;
import ch.verno.db.entity.mandant.MandantScopedEntity;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "parent", schema = "public")
@EntityListeners(MandantEntityListener.class)
public class ParentEntity extends MandantScopedEntity {

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

  @Column(name = "email")
  private String email;

  @Column(name = "phone")
  private String phone;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "gender")
  private GenderEntity gender;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "address")
  private AddressEntity address;

  protected ParentEntity() {
    // JPA
  }

  public ParentEntity(@Nonnull final MandantEntity mandant,
                      @Nonnull final String firstname,
                      @Nonnull final String lastname,
                      @Nonnull final String email,
                      @Nonnull final String phone) {
    setMandant(mandant);
    this.firstname = firstname;
    this.lastname = lastname;
    this.email = email;
    this.phone = phone;
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

  public GenderEntity getGender() {
    return gender;
  }

  public void setGender(final GenderEntity gender) {
    this.gender = gender;
  }

  public AddressEntity getAddress() {
    return address;
  }

  public void setAddress(final AddressEntity address) {
    this.address = address;
  }
}