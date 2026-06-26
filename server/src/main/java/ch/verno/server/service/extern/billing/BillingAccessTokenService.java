package ch.verno.server.service.extern.billing;

import ch.verno.common.exceptions.db.DBNotFoundException;
import ch.verno.common.exceptions.db.DBNotFoundReason;
import ch.verno.contract.dto.table.billing.BillingAccessTokenDto;
import ch.verno.db.entity.billing.BillingAccessTokenEntity;
import ch.verno.lib.Lazy;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.bo.BoFactory;
import ch.verno.server.bo.table.billing.BillingAccessTokenBo;
import ch.verno.server.mapper.billing.BillingAccessTokenMapper;
import ch.verno.server.repository.billing.BillingAccessTokenRepository;
import ch.verno.server.service.base.AbstractEntityServiceLongId;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BillingAccessTokenService extends AbstractEntityServiceLongId<
        BillingAccessTokenEntity,
        BillingAccessTokenDto,
        BillingAccessTokenRepository,
        BillingAccessTokenMapper> {

  @Nonnull private final Lazy<BillingAccessTokenBo> tokenBo;

  public BillingAccessTokenService(@Nonnull final ServerBean bean) {
    super(bean.get(BillingAccessTokenRepository.class), bean.get(BillingAccessTokenMapper.class));
    this.tokenBo = Lazy.of(() -> bean.get(BoFactory.class).get(BillingAccessTokenBo.class));
  }

  @Nonnull
  @Transactional(readOnly = true)
  public BillingAccessTokenDto findByTokenHash(@Nonnull final String tokenHash) {
    return getRepository()
            .findByTokenHash(tokenHash)
            .map(getMapper()::toSimpleDto)
            .orElseThrow(() -> new DBNotFoundException(DBNotFoundReason.BILLING_ACCESS_TOKEN_BY_TOKEN_HASH_NOT_FOUND));
  }

  @Nonnull
  @Override
  public BillingAccessTokenDto save(@Nonnull final BillingAccessTokenDto dto) {
    if (dto.getId() == null) {
      return getMapper().toSimpleDto(tokenBo.get().create(dto));
    }

    return getMapper().toSimpleDto(tokenBo.get().update(dto));
  }

  @Nonnull
  public BillingAccessTokenDto markAsUsed(@Nonnull final String tokenHash) {
    return getMapper().toSimpleDto(tokenBo.get().markAsUsed(tokenHash));
  }

  public boolean existsByTokenHash(@Nonnull final String tokenHash) {
    return tokenBo.get().existsByTokenHash(tokenHash);
  }

  public boolean isExpired(@Nonnull final String tokenHash) {
    return tokenBo.get().isExpired(tokenHash);
  }

  public boolean isUsed(@Nonnull final String tokenHash) {
    return tokenBo.get().isUsed(tokenHash);
  }
}