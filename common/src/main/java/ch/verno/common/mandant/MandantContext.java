package ch.verno.common.mandant;

import com.vaadin.flow.component.UI;
import jakarta.annotation.Nonnull;

public final class MandantContext {

  private static final ThreadLocal<Long> CURRENT = new ThreadLocal<>();

  private MandantContext() {
  }

  public static void set(@Nonnull final Long mandantId) {
    CURRENT.set(mandantId);
  }

  @Nonnull
  public static Long getRequired() {
    final var id = CURRENT.get();

    if (id == null) {
      final var iu = UI.getCurrent();
      throw new IllegalStateException("No mandant set for request");
    }

    return id;
  }

  public static void clear() {
    CURRENT.remove();
  }
}