package ch.verno.ui.verno.settings.panels.gender;

import ch.verno.common.db.constants.text.TextConstants;
import ch.verno.common.db.dto.base.BaseDto;
import ch.verno.common.db.dto.table.GenderDto;
import ch.verno.common.db.dto.table.text.TextDto;
import ch.verno.common.lib.gender.GenderUtil;
import ch.verno.lib.New;
import ch.verno.lib.language.Language;
import ch.verno.lib.language.LanguageUtil;
import jakarta.annotation.Nonnull;

import java.util.List;
import java.util.Map;

public class GenderSettingDto extends BaseDto {

  @Nonnull private final Language currentUserLanguage;
  @Nonnull private List<GenderDto> genders;

  public GenderSettingDto(@Nonnull final Language currentUserLanguage,
                          @Nonnull final List<GenderDto> genders) {
    this.currentUserLanguage = currentUserLanguage;
    this.genders = genders;
  }

  @Nonnull
  public static GenderSettingDto empty() {
    return new GenderSettingDto(LanguageUtil.getDefaultLanguage(), New.arrayList());
  }

  public void add(@Nonnull GenderDto gender) {
    this.genders.add(gender);
  }

  @Nonnull
  public List<GenderDto> getGender() {
    return genders;
  }

  public void setGenders(@Nonnull final List<GenderDto> genders) {
    this.genders = genders;
  }

  @Nonnull
  public Map<Language, String> getDisplayTexts(@Nonnull final GenderDto genderDto) {
    final var texts = New.<Language, String>hashMap();

    final var genderTextDtos = genderDto.getUserDisplayTexts();
    if (genderTextDtos != null && !genderTextDtos.isEmpty()) {
      genderTextDtos.forEach((lang, text) -> texts.put(lang, text.getText()));
      return texts;
    }

    final var gender = GenderUtil.getGenderFromInternalName(genderDto.getName());
    final var languageDescription = GenderUtil.getDescriptionFromLanguage(gender, currentUserLanguage);

    texts.put(currentUserLanguage, languageDescription);

    return texts;
  }

  public void setDisplayTexts(@Nonnull final String genderName, @Nonnull final Map<Language, String> displayTexts) {
    for (final var gender : genders) {
      if (gender.getName().equals(genderName)) {
        gender.setUserDisplayTexts(createTextDto(gender, displayTexts));
      }
    }
  }

  @Nonnull
  public Map<Language, TextDto> createTextDto(@Nonnull final GenderDto genderDto,
                                              @Nonnull final Map<Language, String> displayTexts) {
    final var texts = New.<Language, TextDto>hashMap();
    displayTexts.forEach((lang, text) -> {
      final var textDto = new TextDto(TextConstants.GENDER_IDENTIFIER, genderDto.getName(), lang, text);
      texts.put(lang, textDto);
    });
    return texts;
  }

  public boolean hasMissingUserDisplayTexts() {
    for (final var gender : genders) {
      if (gender.getUserDisplayTexts() == null || gender.getUserDisplayTexts().isEmpty()) {
        return true;
      }
    }

    return false;
  }

}
