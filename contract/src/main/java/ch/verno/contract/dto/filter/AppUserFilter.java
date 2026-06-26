package ch.verno.contract.dto.filter;

import ch.verno.lib.Publ;
import jakarta.annotation.Nonnull;

public record AppUserFilter(@Nonnull String searchText) implements BaseFilter {

  @Nonnull
  public static AppUserFilter empty() {
    return new AppUserFilter(Publ.EMPTY_STRING);
  }
}
