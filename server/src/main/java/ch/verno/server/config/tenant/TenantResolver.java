package ch.verno.server.config.tenant;

import ch.verno.common.exceptions.server.tenant.TenantNotResolvedException;
import ch.verno.lib.VernoConstants;
import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;

public class TenantResolver {

  @Nonnull
  public Optional<Long> resolveTenantId(@Nonnull final HttpServletRequest request) {
    final var value = request.getHeader(VernoConstants.X_MANDANT);

    if (value == null || value.isBlank()) {
      return Optional.empty();
    }

    try {
      return Optional.of(Long.parseLong(value.trim()));
    } catch (final NumberFormatException exception) {
      throw new TenantNotResolvedException(
              "Invalid tenant header: " + value,
              exception
      );
    }
  }
}