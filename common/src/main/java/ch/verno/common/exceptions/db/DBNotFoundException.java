package ch.verno.common.exceptions.db;

import ch.verno.publ.Publ;
import jakarta.annotation.Nonnull;

public class DBNotFoundException extends IllegalArgumentException {

  public DBNotFoundException(@Nonnull final DBNotFoundReason notFoundReason) {
    super(notFoundReason.getMessage());
  }

  public DBNotFoundException(@Nonnull final DBNotFoundReason notFoundReason,
                             @Nonnull final Long id) {
    super(buildMessage(notFoundReason, id));
  }

  @Nonnull
  private static String buildMessage(@Nonnull final DBNotFoundReason reason,
                                     @Nonnull final Long id) {
    final var base = reason.getMessage();
    return base.endsWith(Publ.SPACE)
        ? base + id
        : base + Publ.SPACE + id;
  }
}
