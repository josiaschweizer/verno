package ch.verno.server.service.intern.table.setting;

import ch.verno.contract.dto.table.setting.AppUserSettingDto;
import ch.verno.db.entity.setting.AppUserSettingEntity;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.mapper.setting.AppUserSettingMapper;
import ch.verno.server.repository.setting.AppUserSettingRepository;
import ch.verno.server.service.base.AbstractEntityService;
import jakarta.annotation.Nonnull;

import java.util.Optional;

public class AppUserSettingService extends AbstractEntityService<
        AppUserSettingEntity,
        AppUserSettingDto,
        AppUserSettingRepository,
        AppUserSettingMapper> {

  public AppUserSettingService(@Nonnull final ServerBean serverBean) {
    super(serverBean.get(AppUserSettingRepository.class), serverBean.get(AppUserSettingMapper.class));
  }

  @Nonnull
  public Optional<AppUserSettingDto> findByUserId(@Nonnull final Long userId) {
    return getRepository().findByUserId(userId)
            .map(getMapper()::toSimpleDto);
  }
}
