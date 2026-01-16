package ch.verno.common.db.dto.table;

import ch.verno.common.db.dto.base.BaseDto;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.Locale;

public class AppUserSettingDto extends BaseDto {

  @Nonnull private Long userId;
  @Nullable private String theme;
  @Nonnull private Locale language;

  public AppUserSettingDto() {
    this.userId = 0L;
    this.language = Locale.forLanguageTag("de");
  }

  public AppUserSettingDto(@Nonnull final Long userId,
                           @Nonnull final String theme,
                           @Nonnull final Locale language) {
    this.userId = userId;
    this.theme = theme;
    this.language = language;
  }

  public AppUserSettingDto(@Nullable final Long id,
                           @Nonnull final Long userId,
                           @Nullable final String theme,
                           @Nullable final Locale language) {
    setId(id);
    this.userId = userId;
    this.theme = theme;
    this.language = language != null ? language : Locale.forLanguageTag("de");
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
  public Locale getLanguage() {
    return language;
  }

  public void setLanguage(@Nullable final Locale language) {
    this.language = language != null ? language : Locale.forLanguageTag("de");
  }

  @Nonnull
  public String getLanguageTag() {
    return language.toLanguageTag();
  }

  public void setLanguageTag(@Nullable final String languageTag) {
    this.language = (languageTag == null || languageTag.isBlank())
            ? Locale.forLanguageTag("de")
            : Locale.forLanguageTag(languageTag);
  }
}