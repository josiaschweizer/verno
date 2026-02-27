package ch.verno.lib;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.Objects;
import java.util.function.Supplier;

public final class Lazy<T> {

  @Nullable private Supplier<T> supplier;
  @Nullable private volatile T value;

  private Lazy(@Nonnull final Supplier<T> supplier) {
    this.supplier = supplier;
  }

  public static <T> Lazy<T> of(@Nonnull final Supplier<T> supplier) {
    return new Lazy<>(supplier);
  }


  @Nonnull
  public T get() {
    T result = value;
    if (result == null) {
      synchronized (this) {
        result = value;
        if (result == null) {
          if (supplier == null) {
            throw new IllegalStateException("Supplier is null but value was never initialized");
          }

          result = Objects.requireNonNull(supplier.get(), "Supplier returned null");
          value = result;
          supplier = null;
        }
      }
    }
    return result;
  }

  public boolean isInitialized() {
    return value != null;
  }
}