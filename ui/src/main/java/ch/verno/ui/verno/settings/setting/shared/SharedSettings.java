package ch.verno.ui.verno.settings.setting.shared;

import ch.verno.common.db.dto.MandantSettingDto;
import ch.verno.common.db.service.IMandantSettingService;
import ch.verno.ui.base.settings.VABaseSetting;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import jakarta.annotation.Nonnull;

import java.util.Optional;

public class SharedSettings extends VABaseSetting<MandantSettingDto> {

  public static final String TITLE_KEY = "setting.shared_settings";
  @Nonnull
  private final IMandantSettingService mandantSettingService;

  public SharedSettings(@Nonnull final IMandantSettingService mandantSettingService) {
    super(TITLE_KEY, true);

    this.mandantSettingService = mandantSettingService;
    this.dto = mandantSettingService.getSingleMandantSetting();
  }

  @Nonnull
  @Override
  protected Component createContent() {
    final var enforceQuantityLimits = settingEntryFactory.createBooleanSetting(
            getTranslation("setting.enforce.quantity.settings"),
            Optional.of(getTranslation("setting.when.enabled.quantity.limits.cannot.be.exceeded")),
            binder,
            MandantSettingDto::isEnforceQuantitySettings,
            MandantSettingDto::setEnforceQuantitySettings
    );
    final var enforceCourseLevel = settingEntryFactory.createBooleanSetting(
            getTranslation("setting.enforce.course.level.on.participant.assignment"),
            Optional.of(getTranslation("setting.when.enabled.participants.can.only.be.assigned.to.courses.that.match.their.course.level")),
            binder,
            MandantSettingDto::isEnforceCourseLevelSettings,
            MandantSettingDto::setEnforceCourseLevelSettings
    );
    final var mainParentSetting = settingEntryFactory.createToggleSetting(
            getTranslation("setting.which.parent.is.the.main.parent"),
            getTranslation("participant.parent_one"),
            getTranslation("participant.parent_two"),
            Optional.of(getTranslation("setting.defines.which.parent.is.considered.the.main.contact.for.a.participant")),
            binder,
            MandantSettingDto::isParentOneMainParent,
            MandantSettingDto::setParentOneMainParent
    );

    final var content = new VerticalLayout(enforceQuantityLimits, enforceCourseLevel, mainParentSetting);
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
