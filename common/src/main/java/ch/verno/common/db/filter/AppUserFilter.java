package ch.verno.common.db.filter;

import jakarta.annotation.Nonnull;

public class AppUserFilter {

  @Nonnull private final String searchText;

  public AppUserFilter(@Nonnull final String searchText){
    this.searchText = searchText;
  }

}
