package ch.verno.ui.verno.settings.panels.subscription;

import ch.verno.common.db.dto.table.billing.TenantBillingDto;
import ch.verno.common.server.service.extern.ITenantBillingService;
import ch.verno.common.db.type.billing.BillingPaymentStatus;
import ch.verno.common.db.type.billing.BillingPlanKey;
import ch.verno.common.db.type.billing.BillingSubscriptionStatus;
import ch.verno.common.gate.GlobalInterface;
import ch.verno.common.lib.url.UrlUtil;
import ch.verno.common.properties.configprovider.VernoBillingConfigProvider;
import ch.verno.common.tenant.TenantContext;
import ch.verno.common.ui.base.components.badge.VABadgeLabelOptions;
import ch.verno.lib.Publ;
import ch.verno.ui.base.components.anchorbutton.VAAnchorButton;
import ch.verno.ui.base.factory.BadgeLabelFactory;
import ch.verno.ui.base.factory.EntryFactory;
import ch.verno.ui.lib.settings.VABaseSetting;
import ch.verno.ui.lib.util.LayoutUtil;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.icon.VaadinIcon;
import jakarta.annotation.Nonnull;

import java.util.Arrays;
import java.util.Optional;

public class SubscriptionSettings extends VABaseSetting<TenantBillingDto> {

  public static final String TITLE_KEY = "setting.subscription.settings";

  public SubscriptionSettings(@Nonnull final GlobalInterface globalInterface) {
    super(globalInterface, TITLE_KEY, false);

    loadDto(globalInterface);
    configureHeader();
  }

  @Nonnull
  @Override
  protected Component createContent() {
    final var entryFactory = new EntryFactory<TenantBillingDto>(globalInterface.getI18NProvider());

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
            UrlUtil.buildUrl(globalInterface.getService(VernoBillingConfigProvider.class).getSubscriptionOverviewUrl(),
                    "payment/info"
            )
    );
    addActionButtons(navButton);
  }

  private void loadDto(@Nonnull final GlobalInterface globalInterface) {
    final var currentTenant = Optional.ofNullable(TenantContext.get()).orElse(Publ.ZERO_LONG);
    final var tenantBillingService = globalInterface.getService(ITenantBillingService.class);
    this.dto = tenantBillingService.getTenantBillingByTenantId(currentTenant);
  }

  @Nonnull
  @Override
  protected Class<TenantBillingDto> getBeanType() {
    return TenantBillingDto.class;
  }

  @Nonnull
  @Override
  protected TenantBillingDto createNewBeanInstance() {
    return new TenantBillingDto();
  }

  @Override
  protected boolean isAlwaysReadOnly() {
    return true;
  }
}
