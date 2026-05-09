package ch.verno.common.properties;

import ch.verno.common.db.dto.table.AppUserDto;
import jakarta.annotation.Nonnull;

import java.util.Optional;

public interface UserProperties {

  @Nonnull
  Optional<AppUserDto> getOptionalCurrentUser();

  @Nonnull
  AppUserDto getCurrentUser();

  void logout();

}
