package ch.verno.common.db.service.extern;

import ch.verno.common.db.dto.table.billing.BillingWebhookEventDto;
import jakarta.annotation.Nonnull;

import java.util.List;

public interface IBillingWebhookEventService {

  @Nonnull
  BillingWebhookEventDto updateBillingWebhookEvent(@Nonnull BillingWebhookEventDto dto);

  @Nonnull
  BillingWebhookEventDto createBillingWebhookEvent(@Nonnull BillingWebhookEventDto dto);

  @Nonnull
  BillingWebhookEventDto getBillingWebhookEventById(@Nonnull Long id);

  @Nonnull
  BillingWebhookEventDto getBillingWebhookEventByStripeEventId(@Nonnull String stripeEventId);

  @Nonnull
  List<BillingWebhookEventDto> getBillingWebhookEvents();

  @Nonnull
  BillingWebhookEventDto markBillingWebhookEventAsProcessed(@Nonnull String stripeEventId);

  @Nonnull
  BillingWebhookEventDto updateBillingWebhookEventStatus(@Nonnull String stripeEventId,
                                                         @Nonnull String status);

  @Nonnull
  BillingWebhookEventDto saveBillingWebhookEvent(@Nonnull BillingWebhookEventDto dto);

}
