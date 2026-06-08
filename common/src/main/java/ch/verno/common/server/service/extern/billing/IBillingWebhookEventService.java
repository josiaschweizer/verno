package ch.verno.common.server.service.extern.billing;

import ch.verno.common.db.dto.table.billing.BillingWebhookEventDto;
import jakarta.annotation.Nonnull;

import java.util.List;
import java.util.Optional;

public interface IBillingWebhookEventService {

  @Nonnull
  BillingWebhookEventDto updateBillingWebhookEvent(@Nonnull BillingWebhookEventDto dto);

  @Nonnull
  BillingWebhookEventDto createBillingWebhookEvent(@Nonnull BillingWebhookEventDto dto);

  @Nonnull
  BillingWebhookEventDto getBillingWebhookEventById(@Nonnull Long id);

  @Nonnull
  Optional<BillingWebhookEventDto> getOptionalBillingWebhookEventById(@Nonnull Long id);

  @Nonnull
  BillingWebhookEventDto getBillingWebhookEventByStripeEventId(@Nonnull String stripeEventId);

  @Nonnull
  Optional<BillingWebhookEventDto> getOptionalBillingWebhookEventByStripeEventId(@Nonnull String stripeEventId);

  @Nonnull
  List<BillingWebhookEventDto> getBillingWebhookEvents();

  @Nonnull
  BillingWebhookEventDto markBillingWebhookEventAsProcessed(@Nonnull String stripeEventId);

  @Nonnull
  BillingWebhookEventDto updateBillingWebhookEventStatus(@Nonnull String stripeEventId,
                                                         @Nonnull String status);

  @Nonnull
  BillingWebhookEventDto saveBillingWebhookEvent(@Nonnull BillingWebhookEventDto dto);

  boolean isAlreadyProcessed(@Nonnull String stripeEventId);

  void markProcessed(@Nonnull String stripeEventId);

  void markFailed(@Nonnull String stripeEventId, @Nonnull String message);

  void resetToReceived(@Nonnull String id);
}
