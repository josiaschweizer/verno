package ch.verno.lib.exception;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.jetbrains.annotations.NonNls;

public final class ExceptionUtil {

  /**
   * Converts any exception into an unchecked exception.
   * If it's already unchecked, it is returned as-is (no double wrapping).
   */
  @Nonnull
  public static RuntimeException toUnchecked(@Nonnull final Exception e) {
    if (e instanceof RuntimeException re) {
      return re;
    }

    return new RuntimeException(e);
  }

  /**
   * Like toUnchecked, but with a custom message instead of the original exception's message.
   */
  @Nonnull
  public static RuntimeException toUnchecked(@Nonnull @NonNls final String message,
                                             @Nonnull final Exception e) {
    if (e instanceof RuntimeException runtime) {
      return runtime;
    }

    return new RuntimeException(message, e);
  }

  @Nonnull
  public static RuntimeException toEntityNotFoundException() {
    return toEntityNotFoundException(null);
  }

  @Nonnull
  public static RuntimeException toEntityNotFoundException(@Nullable final String message) {
    return toUnchecked(new EntityNotFoundException(message));
  }

  /**
   * Throws the exception directly, so you don't need to write "throw" yourself.
   * Useful in places where the compiler expects a "return" or "throw",
   * e.g. inside a functional interface without a throws clause.
   */
  @Nonnull
  public static RuntimeException throwUnchecked(@Nonnull final Exception e) {
    throw toUnchecked(e);
  }

  /**
   * Like throwUnchecked, but with a custom message instead of the original exception's message.
   *
   * @param message the message to use for the wrapped exception
   * @param e       the exception to throw
   */
  public static void throwUnchecked(@Nonnull @NonNls final String message,
                                    @Nonnull final Exception e) {
    throw toUnchecked(message, e);
  }

  /**
   * Throws the original checked exception "as-is" using a sneaky throw,
   * without wrapping it in a RuntimeException. Useful when you want to
   * preserve the original exception (including its type), but the
   * surrounding method context doesn't allow a "throws" clause
   * (e.g. in lambdas/streams).
   */
  @SuppressWarnings("unchecked")
  public static <T extends Throwable> RuntimeException sneakyThrow(@Nonnull final Throwable t) throws T {
    throw (T) t;
  }

  /**
   * Rethrows checked exceptions unchanged, but does not wrap anything else
   * (e.g. Error) - helpful when you want to deliberately distinguish
   * between RuntimeException and checked exceptions.
   */
  public static void rethrowIfUnchecked(@Nonnull final Throwable t) {
    if (t instanceof RuntimeException re) {
      throw re;
    }
    if (t instanceof Error err) {
      throw err;
    }
  }

  /**
   * Executes an action that may throw a checked exception,
   * and automatically wraps it into an unchecked exception.
   * Useful for one-liners without try/catch boilerplate.
   */
  public static void run(@Nonnull final ThrowingRunnable runnable) {
    try {
      runnable.run();
    } catch (Exception e) {
      throw toUnchecked(e);
    }
  }

  /**
   * Like run(), but with a return value.
   */
  public static <T> T call(@Nonnull final ThrowingSupplier<T> supplier) {
    try {
      return supplier.get();
    } catch (Exception e) {
      throw toUnchecked(e);
    }
  }

  /**
   * Returns the original root cause of an exception
   * (follows the cause chain to the end).
   */
  public static Throwable getRootCause(@Nonnull final Throwable t) {
    Throwable cause = t;
    while (cause.getCause() != null && cause.getCause() != cause) {
      cause = cause.getCause();
    }
    return cause;
  }

  @FunctionalInterface
  public interface ThrowingRunnable {

    void run() throws Exception;

  }

  @FunctionalInterface
  public interface ThrowingSupplier<T> {

    T get() throws Exception;

  }
}