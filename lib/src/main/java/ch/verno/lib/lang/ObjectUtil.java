package ch.verno.lib.lang;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
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

  public static boolean isEmpty(@Nullable final Object value) {
    if (value == null) {
      return true;
    } else if (value instanceof CharSequence sequence) {
      return sequence.isEmpty();
    } else if (value instanceof Collection<?> collection) {
      return collection.isEmpty();
    } else if (value instanceof Map<?, ?> map) {
      return map.isEmpty();
    } else if (value instanceof Optional<?> optional) {
      return optional.isEmpty();
    } else if (value.getClass().isArray()) {
      return Array.getLength(value) == 0;
    } else if (value instanceof Boolean bool) {
      return !bool;
    } else if (value instanceof Character character) {
      return character == '\0';
    } else if (value instanceof Byte number) {
      return number == 0;
    } else if (value instanceof Short number) {
      return number == 0;
    } else if (value instanceof Integer number) {
      return number == 0;
    } else if (value instanceof Long number) {
      return number == 0L;
    } else if (value instanceof Float number) {
      return number == 0.0f;
    } else if (value instanceof Double number) {
      return number == 0.0d;
    } else if (value instanceof BigInteger number) {
      return BigInteger.ZERO.equals(number);
    } else if (value instanceof BigDecimal number) {
      return number.compareTo(BigDecimal.ZERO) == 0;
    }

    return false;
  }
}