package ch.verno.common.db.service;

import ch.verno.common.db.dto.table.AppUserDto;
import ch.verno.common.db.filter.AppUserFilter;
import com.vaadin.flow.data.provider.QuerySortOrder;
import jakarta.annotation.Nonnull;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface IAppUserService extends UserDetailsService {

  @Nonnull
  Optional<AppUserDto> findByUserName(@Nonnull String username);

  @Nonnull
  AppUserDto findAppUserById(@Nonnull Long id);

  @Nonnull
  List<AppUserDto> findUsers(@Nonnull AppUserFilter filter,
                             int offset,
                             int limit,
                             @Nonnull List<QuerySortOrder> sortOrders);

  @Nonnull
  List<AppUserDto> getAllAppUsers();

  void createAppUser(@Nonnull AppUserDto user);

  void updateAppUser(@Nonnull AppUserDto user);

  void changePassword(@Nonnull Long userId, @Nonnull String newPassword);

  void deleteAppUser(@Nonnull Long id);
}