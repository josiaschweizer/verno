package ch.verno.server.rpc.resource.user;

import ch.verno.contract.dto.filter.AppUserFilter;
import ch.verno.contract.dto.table.base.SortOrderDto;
import ch.verno.contract.dto.table.user.AppUserDto;
import ch.verno.contract.dto.ui.user.UserDtoUnhashedPw;
import ch.verno.contract.endpoint.user.AppUserResource;
import ch.verno.contract.rpc.RpcResource;
import ch.verno.lib.Lazy;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.service.intern.table.user.AppUserService;
import jakarta.annotation.Nonnull;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
@RpcResource(AppUserResource.class)
public class AppUserResourceImpl implements AppUserResource {

  @Nonnull private final PasswordEncoder passwordEncoder;
  @Nonnull private final Lazy<AppUserService> appUserService;

  public AppUserResourceImpl(@Nonnull final ServerBean serverBean) {
    this.passwordEncoder = serverBean.get(PasswordEncoder.class);
    this.appUserService = Lazy.of(() -> serverBean.get(AppUserService.class));
  }

  @Nonnull
  @Override
  public Optional<AppUserDto> findByUserId(@Nonnull final Long id) {
    return appUserService.get().findById(id);
  }

  @Nonnull
  @Override
  public Optional<AppUserDto> findByUsername(@Nonnull final String username) {
    return appUserService.get().findByUsername(username);
  }

  @Nonnull
  @Override
  public Optional<AppUserDto> findByUsernameOrEmail(@Nonnull final String usernameOrEmail) {
    return appUserService.get().findByUsernameOrEmail(usernameOrEmail);
  }

  @Nonnull
  @Override
  public List<AppUserDto> getAllUsers() {
    return appUserService.get().findAll();
  }

  @Nonnull
  @Override
  public List<AppUserDto> getUsers(@Nonnull final AppUserFilter filter, final int offset, final int limit, final List<SortOrderDto> sortOrder) {
    return appUserService.get().findAll(filter, offset, limit, sortOrder);
  }

  @Nonnull
  @Override
  public AppUserDto saveUser(@Nonnull final AppUserDto user) {
    return appUserService.get().save(user);
  }

  @Nonnull
  @Override
  public AppUserDto saveUser(@Nonnull final UserDtoUnhashedPw unhashedPwUser) {
    final var hashedPw = passwordEncoder.encode(unhashedPwUser.getPassword());
    final var appUser = unhashedPwUser.toAppUserDtoUnhashedPw();
    appUser.setPasswordHash(hashedPw);
    return saveUser(appUser);
  }

  @Override
  public void deleteUser(@Nonnull final AppUserDto dto) {
    appUserService.get().delete(dto);
  }
}
