package ch.verno.server.bo.table.billing;

import ch.verno.common.exceptions.db.DBNotFoundException;
import ch.verno.common.exceptions.db.DBNotFoundReason;
import ch.verno.common.tenant.TenantContext;
import ch.verno.common.type.billing.BillingSubscriptionStatus;
import ch.verno.contract.dto.table.billing.TenantBillingDto;
import ch.verno.db.entity.billing.TenantBillingEntity;
import ch.verno.lib.Lazy;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.mapper.billing.TenantBillingMapper;
import ch.verno.server.repository.billing.TenantBillingRepository;
import jakarta.annotation.Nonnull;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.Optional;

@Component
public class TenantBillingBo {

  @Nonnull private final TenantBillingMapper mapper;
  @Nonnull private final Lazy<TenantBillingRepository> repository;

  protected TenantBillingBo(@Nonnull final ServerBean bean) {
    this.mapper = bean.get(TenantBillingMapper.class);
    this.repository = Lazy.of(() -> bean.get(TenantBillingRepository.class));
  }

  @Nonnull
  public TenantBillingEntity create(@Nonnull final TenantBillingDto dto) {
    final var tenantId = requireTenantId(dto);

    try {
      TenantContext.set(tenantId);
      return repository.get().save(mapper.toNewEntity(dto));
    } catch (final DataIntegrityViolationException exception) {
      final var existing = repository.get().findByTenantId(tenantId);

      if (existing.isPresent()) {
        return update(dto);
      }

      throw exception;
    }
  }

  @Nonnull
  public TenantBillingEntity update(@Nonnull final TenantBillingDto dto) {
    final var tenantId = requireTenantId(dto);
    TenantContext.set(tenantId);

    final var entity = repository.get()
            .findByTenantId(tenantId)
            .orElseThrow(() -> new DBNotFoundException(DBNotFoundReason.TENANT_BILLING_BY_TENANT_ID_NOT_FOUND));
    mapper.updateEntity(entity, dto);

    return repository.get().save(entity);
  }

  public boolean hasTenantValidSubscription(@Nonnull final Long tenantId) {
    final var billing = repository.get().findByTenantId(tenantId);

    if (billing.isEmpty()) {
      return false;
    }

    final var dto = mapper.toSimpleDto(billing.get());
    if (BillingSubscriptionStatus.ACTIVE.equals(dto.getSubscriptionStatus())) {
      return true;
    } else if (BillingSubscriptionStatus.TRIAL.equals(dto.getSubscriptionStatus())) {
      return true;
    } else if (BillingSubscriptionStatus.PAST_DUE.equals(dto.getSubscriptionStatus())) {
      return dto.getGraceUntil() != null && dto.getGraceUntil().isAfter(OffsetDateTime.now());
    }

    return false;
  }

  @Nonnull
  private Long requireTenantId(@Nonnull final TenantBillingDto dto) {
    if (dto.getTenantId() == null) {
      throw new IllegalStateException("tenantId must not be null");
    }

    return dto.getTenantId();
  }
}