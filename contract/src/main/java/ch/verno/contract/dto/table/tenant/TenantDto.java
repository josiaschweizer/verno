package ch.verno.contract.dto.table.tenant;

import ch.verno.lib.Publ;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public record TenantDto(@Nonnull Long id,
                        @Nonnull String slug,
                        @Nullable String name) {

  private TenantDto(@Nonnull final Long id) {
    this(id, Publ.EMPTY_STRING, null);
  }

  @Nonnull
  public static TenantDto empty(@Nonnull final Long tenantId) {
    return new TenantDto(tenantId);
  }

  @Nonnull
  @Override
  public String toString() {
    if (name == null || name.isBlank()) {
      return slug + " (ID: " + id + ")";
    }

    return name + " [" + slug + ", ID: " + id + "]";
  }
}