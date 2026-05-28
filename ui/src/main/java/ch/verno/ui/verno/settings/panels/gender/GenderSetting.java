package ch.verno.ui.verno.settings.panels.gender;

import ch.verno.common.db.service.intern.IGenderService;
import ch.verno.common.db.service.intern.ITextService;
import ch.verno.common.gate.GlobalInterface;
import ch.verno.lib.language.Language;
import ch.verno.ui.base.components.multilanguagefield.VAMultiLanguageField;
import ch.verno.ui.base.settings.VABaseSetting;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import jakarta.annotation.Nonnull;

public class GenderSetting extends VABaseSetting<GenderSettingDto> {

  @Nonnull private final ITextService textService;
  @Nonnull private final IGenderService genderService;

  public GenderSetting(@Nonnull GlobalInterface globalInterface) {
    super(globalInterface, "Test", true);

    this.textService = globalInterface.getService(ITextService.class);
    this.genderService = globalInterface.getService(IGenderService.class);

    loadGenderSettings();
  }

  private void loadGenderSettings() {
    final var genders = genderService.getAllGenders();

    this.dto = new GenderSettingDto(genders);
  }

  @Nonnull
  @Override
  protected Component createContent() {
    final var layout = new VerticalLayout();
    layout.setSizeFull();

    for (final var genderDto : dto.getGender()) {
      final var multiLanguageField = new VAMultiLanguageField(
              globalInterface.getUserProperties().getCurrentUserSetting().getLanguage()
      );
      genderDto.setUserDisplayTexts(genderDto.getUserDisplayTexts());
      layout.add(multiLanguageField);
    }

    return layout;
  }

  @Override
  protected void save() {
    super.save();
  }

  @Nonnull
  @Override
  protected Class<GenderSettingDto> getBeanType() {
    return GenderSettingDto.class;
  }

  @Nonnull
  @Override
  protected GenderSettingDto createNewBeanInstance() {
    return GenderSettingDto.empty();
  }

  @Nonnull
  private Language getCurrentUserLanguage() {
    return globalInterface.getUserProperties().getCurrentUserSetting().getLanguage();
  }
}
