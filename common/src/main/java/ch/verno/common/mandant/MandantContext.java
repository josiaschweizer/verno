package ch.verno.common.mandant;

public final class MandantContext {

  private static final ThreadLocal<Long> CURRENT = new ThreadLocal<>();

  private MandantContext() {
  }

  public static void set(final long mandantId) {
    CURRENT.set(mandantId);
  }

  public static long getRequired() {
    final var id = CURRENT.get();
    if (id == null) throw new IllegalStateException("No mandant set for request");
    return id;
  }

  public static void clear() {
    CURRENT.remove();
  }
}