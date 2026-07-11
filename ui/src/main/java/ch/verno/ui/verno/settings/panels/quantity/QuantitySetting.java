package ch.verno.ui.verno.settings.panels.quantity;

import ch.verno.contract.dto.table.setting.TenantSettingDto;
import ch.verno.lib.Lazy;
import ch.verno.rpc.client.setting.TenantSettingClient;
import ch.verno.ui.lib.settings.VABaseSetting;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import jakarta.annotation.Nonnull;

import java.util.Optional;

public class QuantitySetting extends VABaseSetting<TenantSettingDto> {

  private static final String TITLE_KEY = "setting.quantity_settings";

  @Nonnull private final Lazy<TenantSettingClient> tenantSettingClient;

  @Inject
  public QuantitySetting(@Nonnull final Injector injector) {
    super(injector, TITLE_KEY, true);

    this.tenantSettingClient = Lazy.of(() -> injector.getInstance(TenantSettingClient.class));
    this.dto = tenantSettingClient.get().getCurrentOrDefaultTenantSetting();
  }

  @Nonnull
  @Override
  protected Component createContent() {
    final var courseScheduleQuantity = settingEntryFactory.createQuantitySetting(
            getTranslation("setting.quantity.of.course.days.in.one.course.schedule"),
            Optional.of(getTranslation("setting.defines.the.maximum.number.of.course.days.allowed.within.a.single.course.schedule")),
            binder,
            TenantSettingDto::getCourseDaysPerSchedule,
            TenantSettingDto::setCourseDaysPerSchedule
    );
    final var courseQuantity = settingEntryFactory.createQuantitySetting(
            getTranslation("setting.quantity.of.participants.in.one.course"),
            Optional.of(getTranslation("setting.defines.the.maximum.number.of.participants.allowed.per.course")),
            binder,
            TenantSettingDto::getMaxParticipantsPerCourse,
            TenantSettingDto::setMaxParticipantsPerCourse
    );

    final var content = new VerticalLayout(courseScheduleQuantity, courseQuantity);
    content.setPadding(false);

    return content;
  }

  @Override
  protected void save() {
    if (binder.writeBeanIfValid(dto)) {
      tenantSettingClient.get().saveTenantSetting(dto);
    }
  }

  @Nonnull
  public Class<TenantSettingDto> getBeanType() {
    return TenantSettingDto.class;
  }

  @Nonnull
  @Override
  protected TenantSettingDto createNewBeanInstance() {
    return TenantSettingDto.empty();
  }
}