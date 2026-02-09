package ch.verno.common.db.filter;

import ch.verno.publ.Publ;
import jakarta.annotation.Nonnull;

public class AppUserFilter {

  @Nonnull private final String searchText;

  public AppUserFilter(@Nonnull final String searchText){
    this.searchText = searchText;
  }

  @Nonnull
  public static AppUserFilter empty() {
    return new AppUserFilter(Publ.EMPTY_STRING);
  }


  @Nonnull
  public String getSearchText() {
    return searchText;
  }
}
