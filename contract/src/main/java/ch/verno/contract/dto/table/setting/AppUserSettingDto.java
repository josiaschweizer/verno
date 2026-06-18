package ch.verno.contract.dto.table.setting;

import ch.verno.contract.dto.table.base.BaseDto;
import ch.verno.lib.language.Language;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public class AppUserSettingDto extends BaseDto {

  @Nonnull private Long userId;
  @Nullable private String theme;
  @Nonnull private Language language;

  private AppUserSettingDto() {
    this.userId = 0L;
    this.theme = null;
    this.language = Language.DE;
  }

  public AppUserSettingDto(@Nonnull final Long userId,
                           @Nonnull final String theme,
                           @Nonnull final Language language) {
    this(null, userId, theme, language);
  }

  public AppUserSettingDto(@Nullable final Long id,
                           @Nonnull final Long userId,
                           @Nullable final String theme,
                           @Nullable final Language language) {
    setId(id);
    this.userId = userId;
    this.theme = theme;
    this.language = language != null ? language : Language.DE;
  }

  @Nonnull
  public static AppUserSettingDto empty() {
    return new AppUserSettingDto();
  }

  @Nonnull
  public Long getUserId() {
    return userId;
  }

  public void setUserId(@Nonnull final Long userId) {
    this.userId = userId;
  }

  @Nullable
  public String getTheme() {
    return theme;
  }

  public void setTheme(@Nullable final String theme) {
    this.theme = theme;
  }

  @Nonnull
  public Language getLanguage() {
    return language;
  }

  public void setLanguage(@Nullable final Language language) {
    this.language = language != null ? language : Language.DE;
  }

  @Nonnull
  public String getLanguageTag() {
    return language.getCode();
  }

  public void setLanguageTag(@Nullable final String languageTag) {
    this.language = (languageTag == null || languageTag.isBlank())
            ? Language.DE
            : Language.of(languageTag);
  }
}