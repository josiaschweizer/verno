package ch.verno.ui.verno.settings.setting.shared;

import ch.verno.common.db.dto.MandantSettingDto;
import ch.verno.server.service.MandantSettingService;
import ch.verno.ui.base.settings.VABaseSetting;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import jakarta.annotation.Nonnull;

import java.util.Optional;

public class SharedSettings extends VABaseSetting<MandantSettingDto> {

  @Nonnull
  private final MandantSettingService mandantSettingService;

  public SharedSettings(@Nonnull final MandantSettingService mandantSettingService) {
    super("Shared Settings", true);

    this.mandantSettingService = mandantSettingService;
    this.dto = mandantSettingService.getSingleMandantSetting();
  }

  @Nonnull
  @Override
  protected Component createContent() {
    final var enforceQuantityLimits = settingEntryFactory.createBooleanSetting(
            "Enforce Quantity Settings",
            Optional.of("When enabled, quantity limits cannot be exceeded."),
            binder,
            MandantSettingDto::isEnforceQuantitySettings,
            MandantSettingDto::setEnforceQuantitySettings
    );
    final var enforceCourseLevel = settingEntryFactory.createBooleanSetting(
            "Enforce Course Level on Participant Assignment",
            Optional.of("When enabled, participants can only be assigned to courses that match their course level."),
            binder,
            MandantSettingDto::isEnforceCourseLevelSettings,
            MandantSettingDto::setEnforceCourseLevelSettings
    );

    final var content = new VerticalLayout(enforceQuantityLimits, enforceCourseLevel);
    content.setPadding(false);

    return content;
  }

  @Override
  protected void save() {
    if (binder.writeBeanIfValid(dto)) {
      mandantSettingService.saveSingleMandantSetting(dto);
    }
  }

  @Nonnull
  @Override
  protected Binder<MandantSettingDto> createBinder() {
    return new Binder<>(MandantSettingDto.class);
  }

  @Nonnull
  @Override
  protected MandantSettingDto createNewBeanInstance() {
    return new MandantSettingDto();
  }
}
