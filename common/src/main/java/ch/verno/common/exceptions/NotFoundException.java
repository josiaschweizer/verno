package ch.verno.common.exceptions;

import ch.verno.common.util.Publ;
import jakarta.annotation.Nonnull;

public class NotFoundException extends IllegalArgumentException {

  public NotFoundException(@Nonnull final NotFoundReason notFoundReason) {
    super(notFoundReason.getMessage());
  }

  public NotFoundException(@Nonnull final NotFoundReason notFoundReason,
                           @Nonnull final Long id) {
    super(buildMessage(notFoundReason, id));
  }

  @Nonnull
  private static String buildMessage(@Nonnull final NotFoundReason reason,
                                     @Nonnull final Long id) {
    final var base = reason.getMessage();
    return base.endsWith(Publ.SPACE)
        ? base + id
        : base + Publ.SPACE + id;
  }
}
