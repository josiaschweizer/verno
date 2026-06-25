package ch.verno.ui.verno.settings.panels.subscription;

import ch.verno.common.dto.ui.badge.VABadgeLabelOptions;
import ch.verno.common.lib.url.UrlUtil;
import ch.verno.common.type.billing.BillingPaymentStatus;
import ch.verno.common.type.billing.BillingPlanKey;
import ch.verno.common.type.billing.BillingSubscriptionStatus;
import ch.verno.contract.dto.table.billing.TenantBillingDto;
import ch.verno.lib.Lazy;
import ch.verno.rpc.client.billing.BillingClient;
import ch.verno.ui.base.components.anchorbutton.VAAnchorButton;
import ch.verno.ui.base.factory.BadgeLabelFactory;
import ch.verno.ui.base.factory.EntryFactory;
import ch.verno.ui.lib.settings.VABaseSetting;
import ch.verno.ui.lib.util.LayoutUtil;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.i18n.I18NProvider;
import jakarta.annotation.Nonnull;

import java.util.Arrays;
import java.util.Optional;

public class SubscriptionSettings extends VABaseSetting<TenantBillingDto> {

  public static final String TITLE_KEY = "setting.subscription.settings";

  @Nonnull private final Lazy<I18NProvider> i18NProvider;
  @Nonnull private final Lazy<BillingClient> billingClient;

  @Inject
  public SubscriptionSettings(@Nonnull final Injector injector) {
    super(injector, TITLE_KEY, false);

    this.i18NProvider = Lazy.of(() -> injector.getInstance(I18NProvider.class));
    this.billingClient = Lazy.of(() -> injector.getInstance(BillingClient.class));

    loadDto();
    configureHeader();
  }

  @Nonnull
  @Override
  protected Component createContent() {
    final var entryFactory = new EntryFactory<TenantBillingDto>(i18NProvider.get());

    final var plan = entryFactory.createComboBoxEntry(
            dto -> dto.getPlanKey().name(),
            (dto, key) -> dto.setPlanKey(BillingPlanKey.valueOf(key)),
            binder,
            Optional.empty(),
            getTranslation("setting.plan.key"),
            Arrays.stream(BillingPlanKey.values())
                    .map(BillingPlanKey::name)
                    .toList(),
            source -> getTranslation(BillingPlanKey.valueOf(source).getPlanTranslationKey())
    );
    final var subscriptionStatus = entryFactory.createComboBoxEntry(
            dto -> dto.getSubscriptionStatus().getKey(),
            (dto, value) -> dto.setSubscriptionStatus(BillingSubscriptionStatus.fromKey(value)),
            binder,
            Optional.empty(),
            getTranslation("setting.subscription.status"),
            Arrays.stream(BillingSubscriptionStatus.values())
                    .map(BillingSubscriptionStatus::getKey)
                    .toList(),
            source -> getTranslation(BillingSubscriptionStatus.fromKey(source).getTranslationKey())
    );
    final var paymentStatus = entryFactory.createComboBoxEntry(
            dto -> dto.getPaymentStatus().getKey(),
            (dto, value) -> dto.setPaymentStatus(BillingPaymentStatus.fromKey(value)),
            binder,
            Optional.empty(),
            getTranslation("setting.payment.status"),
            Arrays.stream(BillingPaymentStatus.values())
                    .map(BillingPaymentStatus::getKey)
                    .toList(),
            source -> getTranslation(BillingPaymentStatus.fromKey(source).getTranslationKey())
    );

    return LayoutUtil.createHorizontal(plan, subscriptionStatus, paymentStatus);
  }

  private void configureHeader() {
    final var isConfigValid = dto.getSubscriptionStatus().equals(BillingSubscriptionStatus.ACTIVE) &&
            dto.getPaymentStatus().equals(BillingPaymentStatus.PAID);

    if (isConfigValid) {
      setHeaderBadge(BadgeLabelFactory.createBadgeLabel(
              getTranslation("setting.valid.configuration"),
              VABadgeLabelOptions.SUCCESS
      ));
    } else {
      setHeaderBadge(BadgeLabelFactory.createBadgeLabel(
              getTranslation("setting.invalid.configuration"),
              VABadgeLabelOptions.ERROR
      ));
    }

    final var navButton = new VAAnchorButton(
            VaadinIcon.EXTERNAL_LINK.create(),
            UrlUtil.buildUrl(billingClient.get().getSubscriptionOverviewUrl(),
                    "payment/info"
            )
    );
    addActionButtons(navButton);
  }

  private void loadDto() {
    this.dto = billingClient.get().getTenantBillingForCurrentTenant();
  }

  @Nonnull
  @Override
  protected Class<TenantBillingDto> getBeanType() {
    return TenantBillingDto.class;
  }

  @Nonnull
  @Override
  protected TenantBillingDto createNewBeanInstance() {
    return TenantBillingDto.empty();
  }

  @Override
  protected boolean isAlwaysReadOnly() {
    return true;
  }
}
