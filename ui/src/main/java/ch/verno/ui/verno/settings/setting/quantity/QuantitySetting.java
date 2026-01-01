package ch.verno.ui.verno.settings.setting.quantity;

import ch.verno.common.db.dto.MandantSettingDto;
import ch.verno.server.service.MandantSettingService;
import ch.verno.ui.base.settings.VABaseSetting;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import jakarta.annotation.Nonnull;

import java.util.Optional;

public class QuantitySetting extends VABaseSetting<MandantSettingDto> {

  public static final String TITLE_KEY = "setting.quantity_settings";
  @Nonnull
  private final MandantSettingService mandantSettingService;

  public QuantitySetting(@Nonnull MandantSettingService mandantSettingService) {
    super(TITLE_KEY, true);

    this.mandantSettingService = mandantSettingService;
    this.dto = mandantSettingService.getSingleMandantSetting();
  }

  @Nonnull
  @Override
  protected Component createContent() {
    final var courseScheduleQuantity = settingEntryFactory.createQuantitySetting(
            getTranslation("setting.quantity.of.course.days.in.one.course.schedule"),
            Optional.of(getTranslation("setting.defines.the.maximum.number.of.course.days.allowed.within.a.single.course.schedule")),
            binder,
            MandantSettingDto::getCourseDaysPerSchedule,
            MandantSettingDto::setCourseDaysPerSchedule
    );
    final var courseQuantity = settingEntryFactory.createQuantitySetting(
            getTranslation("setting.quantity.of.participants.in.one.course"),
            Optional.of(getTranslation("setting.defines.the.maximum.number.of.participants.allowed.per.course")),
            binder,
            MandantSettingDto::getMaxParticipantsPerCourse,
            MandantSettingDto::setMaxParticipantsPerCourse
    );

    final var content = new VerticalLayout(courseScheduleQuantity, courseQuantity);
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