package ch.verno.contract.dto.table.file;

import ch.verno.contract.dto.table.base.BaseDto;
import jakarta.annotation.Nonnull;

public class StoredFileDto extends BaseDto<Long> {

  @Nonnull private String filename;
  @Nonnull private String contentType;
  @Nonnull private Long size;
  @Nonnull private String checksumSha256;

  private StoredFileDto() {
    this.filename = null;
    this.contentType = null;
    this.size = null;
    this.checksumSha256 = null;
  }

  public StoredFileDto(@Nonnull final String filename,
                       @Nonnull final String contentType,
                       @Nonnull final Long size,
                       @Nonnull final String checksumSha256) {
    this.filename = filename;
    this.contentType = contentType;
    this.size = size;
    this.checksumSha256 = checksumSha256;
  }

  public StoredFileDto(@Nonnull final Long id,
                       @Nonnull final String filename,
                       @Nonnull final String contentType,
                       @Nonnull final Long size,
                       @Nonnull final String checksumSha256) {
    setId(id);
    this.filename = filename;
    this.contentType = contentType;
    this.size = size;
    this.checksumSha256 = checksumSha256;
  }

  @Nonnull
  public static StoredFileDto empty() {
    return new StoredFileDto();
  }

  @Nonnull
  public static StoredFileDto ref(@Nonnull final Long id) {
    final var empty = StoredFileDto.empty();
    empty.setId(id);
    return empty;
  }

  @Nonnull
  public String getFilename() {
    return filename;
  }

  public void setFilename(@Nonnull final String filename) {
    this.filename = filename;
  }

  @Nonnull
  public String getContentType() {
    return contentType;
  }

  public void setContentType(@Nonnull final String contentType) {
    this.contentType = contentType;
  }

  @Nonnull
  public Long getSize() {
    return size;
  }

  public void setSize(@Nonnull final Long size) {
    this.size = size;
  }

  @Nonnull
  public String getChecksumSha256() {
    return checksumSha256;
  }

  public void setChecksumSha256(@Nonnull final String checksumSha256) {
    this.checksumSha256 = checksumSha256;
  }
}