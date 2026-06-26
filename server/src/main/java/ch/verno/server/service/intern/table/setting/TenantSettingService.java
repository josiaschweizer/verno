package ch.verno.server.service.intern.table.setting;

import ch.verno.contract.dto.table.setting.TenantSettingDto;
import ch.verno.db.entity.setting.TenantSettingEntity;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.mapper.setting.TenantSettingMapper;
import ch.verno.server.repository.setting.TenantSettingRepository;
import ch.verno.server.service.base.AbstractEntityServiceLongId;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TenantSettingService extends AbstractEntityServiceLongId<
        TenantSettingEntity,
        TenantSettingDto,
        TenantSettingRepository,
        TenantSettingMapper> {

  public TenantSettingService(@Nonnull final ServerBean serverBean) {
    super(serverBean.get(TenantSettingRepository.class), serverBean.get(TenantSettingMapper.class));
  }

  @Nonnull
  public Optional<TenantSettingDto> findByTenantId(@Nonnull final Long tenantId) {
    return getRepository().getByTenantId(tenantId).map(getMapper()::toSimpleDto);
  }

}
