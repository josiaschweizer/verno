package ch.verno.common.properties;

import ch.verno.common.db.dto.table.AppUserDto;
import ch.verno.common.db.dto.table.AppUserSettingDto;
import jakarta.annotation.Nonnull;
import org.springframework.security.core.userdetails.User;

import javax.annotation.Nullable;
import java.util.Optional;

public interface UserProperties {

  @Nonnull
  Optional<AppUserDto> getOptionalCurrentUser();

  @Nonnull
  AppUserDto getCurrentUser();

  @Nonnull
  AppUserSettingDto getCurrentUserSetting();

  @Nullable
  User getCurrentSpringUser();

  @Nonnull
  User getCurrentSpringUserNonnull();

  void logout();

}
