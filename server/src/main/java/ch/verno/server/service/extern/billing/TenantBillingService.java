package ch.verno.server.service.extern.billing;

import ch.verno.common.exceptions.db.DBNotFoundException;
import ch.verno.common.exceptions.db.DBNotFoundReason;
import ch.verno.contract.dto.table.billing.TenantBillingDto;
import ch.verno.db.entity.billing.TenantBillingEntity;
import ch.verno.lib.Lazy;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.bo.BoFactory;
import ch.verno.server.bo.table.billing.TenantBillingBo;
import ch.verno.server.mapper.billing.TenantBillingMapper;
import ch.verno.server.repository.billing.TenantBillingRepository;
import ch.verno.server.service.base.AbstractEntityServiceLongId;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class TenantBillingService extends AbstractEntityServiceLongId<
        TenantBillingEntity,
        TenantBillingDto,
        TenantBillingRepository,
        TenantBillingMapper> {

  @Nonnull private final Lazy<TenantBillingBo> tenantBillingBo;

  public TenantBillingService(@Nonnull final ServerBean bean) {
    super(
            bean.get(TenantBillingRepository.class),
            bean.get(TenantBillingMapper.class)
    );

    this.tenantBillingBo = Lazy.of(() -> bean.get(BoFactory.class).get(TenantBillingBo.class));
  }

  @Nonnull
  @Transactional(readOnly = true)
  public TenantBillingDto findByTenantId(@Nonnull final Long tenantId) {
    return findOptionalByTenantId(tenantId)
            .orElseThrow(() -> new DBNotFoundException(DBNotFoundReason.TENANT_BILLING_BY_TENANT_ID_NOT_FOUND));
  }

  @Nonnull
  @Transactional(readOnly = true)
  public Optional<TenantBillingDto> findOptionalByTenantId(@Nonnull final Long tenantId) {
    return getRepository()
            .findByTenantId(tenantId)
            .map(getMapper()::toSimpleDto);
  }

  @Nonnull
  @Transactional(readOnly = true)
  public Optional<TenantBillingDto> findOptionalByStripeCustomerId(@Nonnull final String stripeCustomerId) {
    return getRepository()
            .findByStripeCustomerId(stripeCustomerId)
            .map(getMapper()::toSimpleDto);
  }

  @Nonnull
  @Override
  public TenantBillingDto save(@Nonnull final TenantBillingDto dto) {
    if (dto.getId() == null) {
      return getMapper().toSimpleDto(tenantBillingBo.get().create(dto));
    }

    return getMapper().toSimpleDto(tenantBillingBo.get().update(dto));
  }

  public boolean hasTenantValidSubscription(@Nonnull final Long tenantId) {
    return tenantBillingBo.get().hasTenantValidSubscription(tenantId);
  }
}