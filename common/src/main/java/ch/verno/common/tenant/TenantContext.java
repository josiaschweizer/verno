package ch.verno.common.tenant;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.Optional;

public final class TenantContext {

  private static final ThreadLocal<Long> CURRENT = new ThreadLocal<>();

  private TenantContext() {
  }

  public static void set(@Nonnull final Long tenantId) {
    CURRENT.set(tenantId);
  }

  @Nullable
  public static Long get() {
    return CURRENT.get();
  }

  @Nonnull
  public static Long getOrDefault(@Nonnull final Long defaultId) {
    return Optional.ofNullable(get()).orElse(defaultId);
  }

  @Nonnull
  public static Long getRequired() {
    final var id = CURRENT.get();

    if (id == null) {
      throw new IllegalStateException("No tenant set for request");
    }

    return id;
  }

  public static void clear() {
    CURRENT.remove();
  }
}