package ch.verno.rpc.properties.user;

import ch.verno.common.lib.Routes;
import ch.verno.contract.dto.table.user.AppUserDto;
import ch.verno.contract.endpoint.properties.user.UserResource;
import ch.verno.rpc.rpc.RpcFactory;
import ch.verno.ui.base.navigation.Navigator;
import com.vaadin.flow.component.UI;
import jakarta.annotation.Nonnull;

import java.util.Optional;

public class UserProperties {

  @Nonnull private final UserResource userResource;

  public UserProperties(@Nonnull final RpcFactory rpcFactory) {
    this.userResource = rpcFactory.create(UserResource.class);
  }

  @Nonnull
  public AppUserDto getCurrentUser() {
    return userResource.getCurrentUser();
  }

  @Nonnull
  public Optional<AppUserDto> getOptionalCurrentUser() {
    return userResource.getOptionalCurrentUser();
  }

  public void logout() {
    final var ui = UI.getCurrent();
    final var result = userResource.logout();
    if (result) {
      ui.getSession().getSession().invalidate();
      Navigator.navigateTo(Routes.LOGIN);
    }
  }

}
