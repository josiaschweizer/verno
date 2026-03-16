package ch.verno.server.service.extern.billing;

import ch.verno.common.db.dto.table.billing.TenantBillingDto;
import ch.verno.common.db.service.extern.ITenantBillingService;
import ch.verno.common.db.type.billing.BillingSubscriptionStatus;
import ch.verno.common.exceptions.db.DBNotFoundException;
import ch.verno.common.exceptions.db.DBNotFoundReason;
import ch.verno.common.tenant.TenantContext;
import ch.verno.db.entity.billing.TenantBillingEntity;
import ch.verno.server.mapper.billing.TenantBillingMapper;
import ch.verno.server.repository.billing.TenantBillingRepository;
import jakarta.annotation.Nonnull;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TenantBillingService implements ITenantBillingService {

  @Nonnull private final TenantBillingRepository repository;

  public TenantBillingService(@Nonnull final TenantBillingRepository repository) {
    this.repository = repository;
  }

  @Nonnull
  @Override
  @Transactional
  public TenantBillingDto updateTenantBilling(@Nonnull final TenantBillingDto dto) {
    final var tenantId = dto.getTenantId();
    if (tenantId == null) {
      throw new IllegalStateException("tenantId must not be null");
    }

    TenantContext.set(tenantId);

    final var existingBilling = repository.findByTenantId(tenantId)
            .orElseThrow(() -> new DBNotFoundException(DBNotFoundReason.TENANT_BILLING_BY_TENANT_ID_NOT_FOUND));

    TenantBillingMapper.updateEntity(existingBilling, dto);
    final var savedEntity = repository.save(existingBilling);
    return TenantBillingMapper.toDto(savedEntity);
  }

  @Nonnull
  @Override
  @Transactional
  public TenantBillingDto createTenantBilling(@Nonnull final TenantBillingDto dto) {
    if (dto.getTenantId() == null) {
      throw new IllegalStateException("tenantId must not be null");
    }

    final TenantBillingEntity entity = TenantBillingMapper.toEntity(dto, dto.getTenantId());
    if (entity == null) {
      throw new IllegalStateException("TenantBillingDto must not be null");
    }

    try {
      TenantContext.set(entity.getTenant().getId());
      return TenantBillingMapper.toDto(repository.save(entity));
    } catch (DataIntegrityViolationException exception) {
      final var existing = repository.findByTenantId(dto.getTenantId());

      if (existing.isPresent()) {
        return updateTenantBilling(dto);
      }

      throw exception;
    }
  }

  @Nonnull
  @Override
  @Transactional(readOnly = true)
  public TenantBillingDto getTenantBillingById(@Nonnull final Long id) {
    final var foundById = repository.findById(id);

    if (foundById.isEmpty()) {
      throw new DBNotFoundException(DBNotFoundReason.TENANT_BILLING_BY_ID_NOT_FOUND);
    }

    return TenantBillingMapper.toDto(foundById.get());
  }

  @Nonnull
  @Override
  @Transactional(readOnly = true)
  public TenantBillingDto getTenantBillingByTenantId(@Nonnull final Long tenantId) {
    final var foundByTenantId = getOptionalTenantBillingByTenantId(tenantId);

    if (foundByTenantId.isEmpty()) {
      throw new DBNotFoundException(DBNotFoundReason.TENANT_BILLING_BY_TENANT_ID_NOT_FOUND);
    }

    return foundByTenantId.get();
  }

  @Nonnull
  @Override
  @Transactional(readOnly = true)
  public Optional<TenantBillingDto> getOptionalTenantBillingByTenantId(@Nonnull final Long tenantId) {
    final var foundByTenantId = repository.findByTenantId(tenantId);
    return foundByTenantId.map(TenantBillingMapper::toDto);
  }

  @Nonnull
  @Override
  @Transactional(readOnly = true)
  public Optional<TenantBillingDto> getOptionalTenantBillingByStripeCustomerId(@Nonnull final String stripeCustomerId) {
    final var foundByStripeCustomerId = repository.findByStripeCustomerId(stripeCustomerId);
    return foundByStripeCustomerId.map(TenantBillingMapper::toDto);
  }

  @Nonnull
  @Override
  @Transactional(readOnly = true)
  public List<TenantBillingDto> getTenantBillings() {
    return repository.findAll().stream()
            .map(TenantBillingMapper::toDto)
            .toList();
  }

  @Nonnull
  @Override
  @Transactional
  public TenantBillingDto saveTenantBilling(@Nonnull final TenantBillingDto dto) {
    if (dto.getId() == null) {
      return createTenantBilling(dto);
    }
    return updateTenantBilling(dto);
  }

  @Override
  @Transactional
  public boolean hasTenantValidSubscription(@Nonnull final Long tenantId) {
    final var foundById = repository.findByTenantId(tenantId);
    if (foundById.isEmpty()) {
      return false;
    }

    final var billing = TenantBillingMapper.toDto(foundById.get());
    if (BillingSubscriptionStatus.ACTIVE.equals(billing.getSubscriptionStatus())) {
      return true;
    }

    if (BillingSubscriptionStatus.TRIAL.equals(billing.getSubscriptionStatus())) {
      return true;
    }

    if (BillingSubscriptionStatus.PAST_DUE.equals(billing.getSubscriptionStatus())) {
      return billing.getGraceUntil() != null && billing.getGraceUntil().isAfter(OffsetDateTime.now());
    }

    return false;
  }
}