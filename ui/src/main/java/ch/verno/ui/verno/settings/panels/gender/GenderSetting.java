package ch.verno.ui.verno.settings.panels.gender;

import ch.verno.common.server.service.intern.IGenderService;
import ch.verno.common.gate.GlobalInterface;
import ch.verno.lib.New;
import ch.verno.lib.lib.language.Language;
import ch.verno.lib.Publ;
import ch.verno.ui.base.components.multilanguagefield.VAMultiLanguageField;
import ch.verno.ui.lib.settings.VABaseSetting;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import jakarta.annotation.Nonnull;

import java.util.List;

public class GenderSetting extends VABaseSetting<GenderSettingDto> {

  @Nonnull private final IGenderService genderService;

  public GenderSetting(@Nonnull GlobalInterface globalInterface) {
    super(globalInterface, "Gender Settings", true); //TODO translation

    this.genderService = globalInterface.getService(IGenderService.class);

    loadGenderSettings();
  }

  private void loadGenderSettings() {
    final var genders = genderService.getAllGenders();
    this.dto = new GenderSettingDto(getCurrentUserLanguage(), genders);
  }

  @Override
  protected void onAttach(@Nonnull final AttachEvent attachEvent) {
    super.onAttach(attachEvent);

    if (dto.hasMissingUserDisplayTexts()){
      saveButton.setEnabled(true);
      saveButton.setTooltipText(Publ.EMPTY_STRING);
    }
  }

  @Nonnull
  @Override
  protected Component createContent() {
    final var layout = new VerticalLayout();
    layout.setPadding(false);
    layout.setMargin(false);
    layout.setSizeFull();

    final var currentUserLanguage = getCurrentUserLanguage();
    final var configuredLanguages = getConfiguredLanguages();

    for (final var genderDto : dto.getGender()) {
      final var multiLanguageField = new VAMultiLanguageField(currentUserLanguage, configuredLanguages);
      binder.forField(multiLanguageField)
              .bind(
                      dto -> dto.getDisplayTexts(genderDto),
                      (value, fieldValue) -> dto.setDisplayTexts(genderDto.getName(), fieldValue)
              );

      layout.add(multiLanguageField);
    }

    return layout;
  }

  @Nonnull
  private List<Language> getConfiguredLanguages() {
    final var languages = New.<Language>arrayList();
    languages.add(Language.DE);
    languages.add(Language.EN);
    languages.add(Language.FR);
    return languages;
  }

  @Override
  protected void save() {
    if (binder.writeBeanIfValid(dto)) {
      saveGenders();
    }
  }

  private void saveGenders() {
    for (final var genderDto : dto.getGender()) {
      genderService.saveGender(genderDto);
    }
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
