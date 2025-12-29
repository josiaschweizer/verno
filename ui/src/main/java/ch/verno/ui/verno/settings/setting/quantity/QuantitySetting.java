package ch.verno.ui.verno.settings.setting.quantity;

import ch.verno.common.db.dto.MandantSettingDto;
import ch.verno.server.service.MandantSettingService;
import ch.verno.ui.base.settings.VABaseSetting;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import jakarta.annotation.Nonnull;
import org.jspecify.annotations.NonNull;

public class QuantitySetting extends VABaseSetting<MandantSettingDto> {

  @Nonnull
  private final MandantSettingService mandantSettingService;
  @Nonnull
  private final Button saveButton;

  public QuantitySetting(@Nonnull MandantSettingService mandantSettingService) {
    super("Quantity Settings");
    this.mandantSettingService = mandantSettingService;
    this.dto = mandantSettingService.getSingleMandantSetting();

    saveButton = new Button("Save", e -> save());
    saveButton.setEnabled(false);
    setActionButton(saveButton);
  }

  @Nonnull
  @Override
  protected Component createContent() {
    final var courseScheduleQuantity = settingEntryFactory.createQuantitySetting(
            "Quantity of Course Days in one Course Schedule",
            binder,
            MandantSettingDto::getCourseDaysPerSchedule,
            MandantSettingDto::setCourseDaysPerSchedule
    );
    final var courseQuantity = settingEntryFactory.createQuantitySetting(
            "Quantity of Participants in one Course",
            binder,
            MandantSettingDto::getMaxParticipantsPerCourse,
            MandantSettingDto::setMaxParticipantsPerCourse
    );

    final var content = new VerticalLayout(courseScheduleQuantity, courseQuantity);
    content.setPadding(false);

    binder.readBean(dto);
    binder.addStatusChangeListener(e -> saveButton.setEnabled(binder.hasChanges() && binder.isValid()));
    binder.addValueChangeListener(e -> saveButton.setEnabled(binder.hasChanges() && binder.isValid()));

    return content;
  }

  private void save() {
    if (binder.writeBeanIfValid(dto)) {
      mandantSettingService.saveSingleMandantSetting(dto);
    }
  }

  @Nonnull
  @Override
  protected Binder<MandantSettingDto> createBinder() {
    return new Binder<>(MandantSettingDto.class);
  }

  @NonNull
  @Override
  protected MandantSettingDto createNewBeanInstance() {
    return new MandantSettingDto();
  }
}