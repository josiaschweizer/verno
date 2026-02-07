package ch.verno.ui.verno.settings.setting.quantity;

import ch.verno.common.db.dto.table.TenantSettingDto;
import ch.verno.common.db.service.ITenantSettingService;
import ch.verno.ui.base.settings.VABaseSetting;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import jakarta.annotation.Nonnull;

import java.util.Optional;

public class QuantitySetting extends VABaseSetting<TenantSettingDto> {

  public static final String TITLE_KEY = "setting.quantity_settings";
  @Nonnull
  private final ITenantSettingService tenantSettingService;

  public QuantitySetting(@Nonnull ITenantSettingService tenantSettingService) {
    super(TITLE_KEY, true);

    this.tenantSettingService = tenantSettingService;
    this.dto = tenantSettingService.getCurrentTenantSettingOrDefault();
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
      tenantSettingService.saveCurrentTenantSetting(dto);
    }
  }

  @Nonnull
  public Class<TenantSettingDto> getBeanType() {
    return TenantSettingDto.class;
  }

  @Nonnull
  @Override
  protected TenantSettingDto createNewBeanInstance() {
    return new TenantSettingDto();
  }
}