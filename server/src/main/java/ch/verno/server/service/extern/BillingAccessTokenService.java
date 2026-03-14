package ch.verno.server.service.extern;

import ch.verno.common.db.dto.table.billing.BillingAccessTokenDto;
import ch.verno.common.db.service.extern.IBillingAccessTokenService;
import ch.verno.common.exceptions.db.DBNotFoundException;
import ch.verno.common.exceptions.db.DBNotFoundReason;
import ch.verno.db.entity.billing.BillingAccessTokenEntity;
import ch.verno.db.entity.user.AppUserEntity;
import ch.verno.server.mapper.billing.BillingAccessTokenMapper;
import ch.verno.server.repository.billing.BillingAccessTokenRepository;
import ch.verno.server.repository.AppUserRepository;
import jakarta.annotation.Nonnull;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Service
public class BillingAccessTokenService implements IBillingAccessTokenService {

  @Nonnull private final BillingAccessTokenRepository repository;
  @Nonnull private final AppUserRepository appUserRepository;

  @PersistenceContext
  private EntityManager entityManager;

  public BillingAccessTokenService(@Nonnull final BillingAccessTokenRepository repository,
                                   @Nonnull final AppUserRepository appUserRepository) {
    this.repository = repository;
    this.appUserRepository = appUserRepository;
  }

  @Nonnull
  @Override
  @Transactional
  public BillingAccessTokenDto updateBillingAccessToken(@Nonnull final BillingAccessTokenDto dto) {
    final var id = dto.getId();
    if (id == null) {
      throw new IllegalStateException("id must not be null");
    }

    final var existingToken = repository.findById(id)
            .orElseThrow(() -> new DBNotFoundException(DBNotFoundReason.BILLING_ACCESS_TOKEN_BY_ID_NOT_FOUND));

    BillingAccessTokenMapper.updateEntity(existingToken, dto);
    final var savedEntity = repository.save(existingToken);
    return BillingAccessTokenMapper.toDto(savedEntity);
  }

  @Nonnull
  @Override
  @Transactional
  public BillingAccessTokenDto createBillingAccessToken(@Nonnull final BillingAccessTokenDto dto) {
    final var tenantId = dto.getTenantId();
    final var userId = dto.getUserId();

    if (tenantId == null) {
      throw new IllegalStateException("tenantId must not be null");
    }
    if (userId == null) {
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

    appUserRepository.findById(userId)
            .orElseThrow(() -> new DBNotFoundException(DBNotFoundReason.APP_USER_NOT_FOUND));

    try {
      final var userRef = entityManager.getReference(AppUserEntity.class, userId);
      final BillingAccessTokenEntity entity = BillingAccessTokenMapper.toEntity(dto, tenantId, userRef);

      if (entity == null) {
        throw new IllegalStateException("BillingAccessTokenDto must not be null");
      }

      return BillingAccessTokenMapper.toDto(repository.save(entity));
    } catch (DataIntegrityViolationException exception) {
      final var existing = repository.findByTokenHash(dto.getTokenHash());

      if (existing.isPresent()) {
        return BillingAccessTokenMapper.toDto(existing.get());
      }

      throw exception;
    }
  }

  @Nonnull
  @Override
  @Transactional(readOnly = true)
  public BillingAccessTokenDto getBillingAccessTokenById(@Nonnull final Long id) {
    final var foundById = repository.findById(id);

    if (foundById.isEmpty()) {
      throw new DBNotFoundException(DBNotFoundReason.BILLING_ACCESS_TOKEN_BY_ID_NOT_FOUND);
    }

    return BillingAccessTokenMapper.toDto(foundById.get());
  }

  @Nonnull
  @Override
  @Transactional(readOnly = true)
  public BillingAccessTokenDto getBillingAccessTokenByTokenHash(@Nonnull final String tokenHash) {
    final var foundByTokenHash = repository.findByTokenHash(tokenHash);

    if (foundByTokenHash.isEmpty()) {
      throw new DBNotFoundException(DBNotFoundReason.BILLING_ACCESS_TOKEN_BY_TOKEN_HASH_NOT_FOUND);
    }

    return BillingAccessTokenMapper.toDto(foundByTokenHash.get());
  }

  @Nonnull
  @Override
  @Transactional(readOnly = true)
  public List<BillingAccessTokenDto> getBillingAccessTokens() {
    return repository.findAll().stream()
            .map(BillingAccessTokenMapper::toDto)
            .toList();
  }

  @Nonnull
  @Override
  @Transactional
  public BillingAccessTokenDto markBillingAccessTokenAsUsed(@Nonnull final String tokenHash) {
    final var token = repository.findByTokenHash(tokenHash)
            .orElseThrow(() -> new DBNotFoundException(DBNotFoundReason.BILLING_ACCESS_TOKEN_BY_TOKEN_HASH_NOT_FOUND));

    token.setUsedAt(OffsetDateTime.now());

    final var savedEntity = repository.save(token);
    return BillingAccessTokenMapper.toDto(savedEntity);
  }


  @Override
  @Transactional(readOnly = true)
  public boolean existsBillingAccessTokenByTokenHash(@Nonnull final String tokenHash) {
    return repository.existsByTokenHash(tokenHash);
  }

  @Override
  @Transactional(readOnly = true)
  public boolean isBillingAccessTokenExpired(@Nonnull final String tokenHash) {
    final var dto = getBillingAccessTokenByTokenHash(tokenHash);
    return dto.getExpiresAt() != null && dto.getExpiresAt().isBefore(OffsetDateTime.now());
  }

  @Override
  @Transactional(readOnly = true)
  public boolean isBillingAccessTokenUsed(@Nonnull final String tokenHash) {
    final var dto = getBillingAccessTokenByTokenHash(tokenHash);
    return dto.getUsedAt() != null;
  }

  @Nonnull
  @Override
  @Transactional
  public BillingAccessTokenDto saveBillingAccessToken(@Nonnull final BillingAccessTokenDto dto) {
    if (dto.getId() == null) {
      return createBillingAccessToken(dto);
    }
    return updateBillingAccessToken(dto);
  }
}