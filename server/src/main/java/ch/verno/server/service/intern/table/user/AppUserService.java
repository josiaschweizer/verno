package ch.verno.server.service.intern.table.user;

import ch.verno.contract.dto.table.user.AppUserDto;
import ch.verno.db.entity.user.AppUserEntity;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.mapper.user.AppUserMapper;
import ch.verno.server.repository.user.AppUserRepository;
import ch.verno.server.service.base.AbstractEntityService;
import jakarta.annotation.Nonnull;

import java.util.Optional;

public class AppUserService extends AbstractEntityService<
        AppUserEntity,
        AppUserDto,
        AppUserRepository,
        AppUserMapper> {

  public AppUserService(@Nonnull final ServerBean bean) {
    super(bean.get(AppUserRepository.class), bean.get(AppUserMapper.class));
  }

  @Nonnull
  public Optional<AppUserDto> findByUsername(@Nonnull final String username) {
    return getRepository().findByUsername(username)
            .map(getMapper()::toSimpleDto);
  }

  @Nonnull
  public Optional<AppUserDto> findByUsernameOrEmail(@Nonnull final String nameOrEmail) {
    return getRepository().findByUsernameOrEmail(nameOrEmail)
            .map(getMapper()::toSimpleDto);
  }

  public boolean existsByUsername(@Nonnull final String username,
                                  @Nonnull final Long tenantId) {
    return getRepository().existsByUsernameAndTenantId(username, tenantId);
  }

}
