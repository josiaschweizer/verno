package ch.verno.db.entity;

import ch.verno.db.entity.mandant.MandantEntity;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "address")
public class AddressEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "mandant_id", nullable = false)
  private MandantEntity mandant;

  @Nonnull
  @Column(name = "created_at", nullable = false)
  private OffsetDateTime createdAt = OffsetDateTime.now();

  @Column(name = "street")
  private String street;

  @Column(name = "house_number")
  private String houseNumber;

  @Column(name = "zip_code")
  private String zipCode;

  @Column(name = "city")
  private String city;

  @Column(name = "country")
  private String country;

  protected AddressEntity() {
    // JPA
  }

  public AddressEntity(@Nonnull final MandantEntity mandant,
                       @Nonnull final String street,
                       @Nonnull final String houseNumber,
                       @Nonnull final String zipCode,
                       @Nonnull final String city,
                       @Nonnull final String country) {
    this.mandant = mandant;
    this.street = street;
    this.houseNumber = houseNumber;
    this.zipCode = zipCode;
    this.city = city;
    this.country = country;
  }

  public Long getId() {
    return id;
  }

  public void setId(final Long id) {
    this.id = id;
  }

  public MandantEntity getMandant() {
    return mandant;
  }

  public void setMandant(final MandantEntity mandant) {
    this.mandant = mandant;
  }

  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public String getStreet() {
    return street;
  }

  public void setStreet(final String street) {
    this.street = street;
  }

  public String getHouseNumber() {
    return houseNumber;
  }

  public void setHouseNumber(final String houseNumber) {
    this.houseNumber = houseNumber;
  }

  public String getZipCode() {
    return zipCode;
  }

  public void setZipCode(final String zipCode) {
    this.zipCode = zipCode;
  }

  public String getCity() {
    return city;
  }

  public void setCity(final String city) {
    this.city = city;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(final String country) {
    this.country = country;
  }
}