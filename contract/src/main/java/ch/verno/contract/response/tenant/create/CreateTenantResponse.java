package ch.verno.contract.response.tenant.create;

import ch.verno.lib.VernoConstants;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public record CreateTenantResponse(
        @Nullable Long tenantId,
        @Nonnull String tenantKey,
        @Nonnull String subdomain,
        boolean error,
        @Nonnull String status
) {

  @Nonnull
  public static CreateTenantResponse success(@Nonnull final Long tenantId,
                                             @Nonnull final String tenantKey,
                                             @Nonnull final String subdomain) {
    return new CreateTenantResponse(tenantId, tenantKey, subdomain, false, VernoConstants.STATUS_CREATED);
  }

  @Nonnull
  public static CreateTenantResponse failure(@Nonnull final String tenantKey,
                                             @Nonnull final String subdomain,
                                             @Nonnull final String status) {
    return new CreateTenantResponse(null, tenantKey, subdomain, true, status);
  }

}