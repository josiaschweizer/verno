package ch.verno.lib.lang;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.Objects;
import java.util.function.Supplier;

public final class ObjectUtil {

  private ObjectUtil() {
  }

  public static boolean isNull(@Nullable final Object value) {
    return value == null;
  }

  public static boolean isNotNull(@Nullable final Object value) {
    return value != null;
  }

  @Nullable
  public static <T> T defaultIfNull(@Nullable final T value, @Nullable final T defaultValue) {
    return value == null ? defaultValue : value;
  }

  @Nonnull
  public static <T> T requireNonNull(@Nullable final T value, @Nonnull final String message) {
    return Objects.requireNonNull(value, message);
  }

  @Nullable
  public static <T> T getOrNull(@Nullable final Supplier<T> supplier) {
    if (supplier == null) {
      return null;
    }

    return supplier.get();
  }

  public static boolean equals(@Nullable final Object first, @Nullable final Object second) {
    return Objects.equals(first, second);
  }

  public static boolean notEquals(@Nullable final Object first, @Nullable final Object second) {
    return !equals(first, second);
  }
}