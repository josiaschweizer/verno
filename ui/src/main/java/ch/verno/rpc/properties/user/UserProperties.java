package ch.verno.rpc.properties.user;

import ch.verno.common.lib.Routes;
import ch.verno.contract.endpoint.properties.user.UserResource;
import ch.verno.contract.endpoint.user.AppUserResource;
import ch.verno.lib.Lazy;
import ch.verno.rpc.rpc.RpcFactory;
import ch.verno.ui.base.navigation.Navigator;
import com.google.inject.Inject;
import com.vaadin.flow.component.UI;
import jakarta.annotation.Nonnull;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public class UserProperties {

  @Nonnull private final Lazy<UserResource> userResource;
  @Nonnull private final Lazy<AppUserResource> appUserResource;

  @Inject
  public UserProperties(@Nonnull final RpcFactory rpcFactory) {
    this.userResource = Lazy.of(() -> rpcFactory.create(UserResource.class));
    this.appUserResource = Lazy.of(() -> rpcFactory.create(AppUserResource.class));
  }

  @Nonnull
  public Optional<UserDetails> findOptionalByUsernameOrEmail(@Nonnull final String usernameOrEmail) {
    final var userOptional = appUserResource.get().findByUsernameOrEmail(usernameOrEmail);
    if (userOptional.isEmpty()) {
      return Optional.empty();
    }

    final var user = userOptional.get();
    final var userDetail = User.withUsername(user.getUsername())
            .password(user.getPasswordHash())
            .roles(user.getRole().getRole())
            .build();
    return Optional.of(userDetail);
  }

  public void logout() {
    final var ui = UI.getCurrent();
    final var result = userResource.get().logout();
    if (result) {
      ui.getSession().getSession().invalidate();
      Navigator.navigateTo(Routes.LOGIN);
    }
  }

}
