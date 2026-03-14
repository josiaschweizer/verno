package ch.verno.common.db.service.extern;

import ch.verno.common.db.dto.table.billing.BillingAccessTokenDto;
import jakarta.annotation.Nonnull;

import java.util.List;

public interface IBillingAccessTokenService {

  @Nonnull
  BillingAccessTokenDto updateBillingAccessToken(@Nonnull BillingAccessTokenDto dto);

  @Nonnull
  BillingAccessTokenDto createBillingAccessToken(@Nonnull BillingAccessTokenDto dto);

  @Nonnull
  BillingAccessTokenDto getBillingAccessTokenById(@Nonnull Long id);

  @Nonnull
  BillingAccessTokenDto getBillingAccessTokenByTokenHash(@Nonnull String tokenHash);

  @Nonnull
  List<BillingAccessTokenDto> getBillingAccessTokens();

  @Nonnull
  BillingAccessTokenDto markBillingAccessTokenAsUsed(@Nonnull String tokenHash);

  boolean existsBillingAccessTokenByTokenHash(@Nonnull String tokenHash);

  boolean isBillingAccessTokenExpired(@Nonnull String tokenHash);

  boolean isBillingAccessTokenUsed(@Nonnull String tokenHash);

  @Nonnull
  BillingAccessTokenDto saveBillingAccessToken(@Nonnull BillingAccessTokenDto dto);

}
