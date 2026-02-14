package ch.verno.db.entity.file;

import ch.verno.db.entity.tenant.TenantEntityListener;
import ch.verno.db.entity.tenant.TenantScopedEntity;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "file_store")
@EntityListeners(TenantEntityListener.class)
public class StoredFileEntity extends TenantScopedEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 512)
  private String filename;

  @Column(nullable = false, length = 128)
  private String contentType;

  @Column(nullable = false)
  private long size;

  @Column(nullable = false, length = 128)
  private String checksumSha256;

  @Column(nullable = false, length = 1024)
  private String storageKey;

  @Column(nullable = false)
  private Instant createdAt;

  protected StoredFileEntity() {
  }

  public StoredFileEntity(
          @Nullable final Long id,
          @Nonnull final String filename,
          @Nonnull final String contentType,
          final long size,
          @Nonnull final String checksumSha256,
          @Nullable final String storageKey,
          @Nonnull final Instant createdAt
  ) {
    this.id = id;
    this.filename = filename;
    this.contentType = contentType;
    this.size = size;
    this.checksumSha256 = checksumSha256;
    this.storageKey = storageKey;
    this.createdAt = createdAt;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getFilename() {
    return filename;
  }

  public void setFilename(final String filename) {
    this.filename = filename;
  }

  public String getContentType() {
    return contentType;
  }

  public void setContentType(final String contentType) {
    this.contentType = contentType;
  }

  public long getSize() {
    return size;
  }

  public void setSize(final long size) {
    this.size = size;
  }

  public String getChecksumSha256() {
    return checksumSha256;
  }

  public void setChecksumSha256(final String checksumSha256) {
    this.checksumSha256 = checksumSha256;
  }

  public String getStorageKey() {
    return storageKey;
  }

  public void setStorageKey(final String storageKey) {
    this.storageKey = storageKey;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(final Instant createdAt) {
    this.createdAt = createdAt;
  }
}