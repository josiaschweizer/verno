package ch.verno.server.bo.table.billing;

import ch.verno.common.exceptions.db.DBNotFoundException;
import ch.verno.common.exceptions.db.DBNotFoundReason;
import ch.verno.contract.dto.table.billing.BillingAccessTokenDto;
import ch.verno.db.entity.billing.BillingAccessTokenEntity;
import ch.verno.lib.Lazy;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.mapper.billing.BillingAccessTokenMapper;
import ch.verno.server.repository.billing.BillingAccessTokenRepository;
import ch.verno.server.repository.user.AppUserRepository;
import jakarta.annotation.Nonnull;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

@Component
public class BillingAccessTokenBo {

  @Nonnull private final Lazy<AppUserRepository> appUserRepository;
  @Nonnull private final Lazy<BillingAccessTokenRepository> repository;
  @Nonnull private final Lazy<BillingAccessTokenMapper> mapper;

  public BillingAccessTokenBo(@Nonnull final ServerBean bean) {
    this.appUserRepository = Lazy.of(() -> bean.get(AppUserRepository.class));
    this.repository = Lazy.of(() -> bean.get(BillingAccessTokenRepository.class));
    this.mapper = Lazy.of(() -> bean.get(BillingAccessTokenMapper.class));
  }

  @Nonnull
  public BillingAccessTokenEntity create(@Nonnull final BillingAccessTokenDto dto) {
    validateForCreate(dto);

    appUserRepository.get()
            .findById(dto.getUserId())
            .orElseThrow(() -> new DBNotFoundException(DBNotFoundReason.APP_USER_NOT_FOUND));

    try {
      return repository.get().save(mapper.get().toNewEntity(dto));
    } catch (final DataIntegrityViolationException exception) {
      return repository.get()
              .findByTokenHash(dto.getTokenHash())
              .orElseThrow(() -> exception);
    }
  }

  @Nonnull
  public BillingAccessTokenEntity update(@Nonnull final BillingAccessTokenDto dto) {
    if (dto.getId() == null) {
      throw new IllegalStateException("id must not be null");
    }

    final var entity = repository.get()
            .findById(dto.getId())
            .orElseThrow(() -> new DBNotFoundException(DBNotFoundReason.BILLING_ACCESS_TOKEN_BY_ID_NOT_FOUND));

    mapper.get().updateEntity(entity, dto);
    return repository.get().save(entity);
  }

  @Nonnull
  public BillingAccessTokenEntity markAsUsed(@Nonnull final String tokenHash) {
    final var entity = repository.get()
            .findByTokenHash(tokenHash)
            .orElseThrow(() -> new DBNotFoundException(DBNotFoundReason.BILLING_ACCESS_TOKEN_BY_TOKEN_HASH_NOT_FOUND));

    entity.setUsedAt(OffsetDateTime.now());
    return repository.get().save(entity);
  }

  public boolean existsByTokenHash(@Nonnull final String tokenHash) {
    return repository.get().existsByTokenHash(tokenHash);
  }

  public boolean isExpired(@Nonnull final String tokenHash) {
    return repository.get()
            .findByTokenHash(tokenHash)
            .orElseThrow(() -> new DBNotFoundException(DBNotFoundReason.BILLING_ACCESS_TOKEN_BY_TOKEN_HASH_NOT_FOUND))
            .isExpired();
  }

  public boolean isUsed(@Nonnull final String tokenHash) {
    return repository.get()
            .findByTokenHash(tokenHash)
            .orElseThrow(() -> new DBNotFoundException(DBNotFoundReason.BILLING_ACCESS_TOKEN_BY_TOKEN_HASH_NOT_FOUND))
            .isUsed();
  }

  private void validateForCreate(@Nonnull final BillingAccessTokenDto dto) {
    if (dto.getTenantId() == null) {
      throw new IllegalStateException("tenantId must not be null");
    }
    if (dto.getUserId() == null) {
      throw new IllegalStateException("userId must not be null");
    }
    if (dto.getTokenHash().isBlank()) {
      throw new IllegalStateException("tokenHash must not be blank");
    }
    if (dto.getPurpose().isBlank()) {
      throw new IllegalStateException("purpose must not be blank");
    }
    if (dto.getExpiresAt() == null) {
      throw new IllegalStateException("expiresAt must not be null");
    }
  }
}