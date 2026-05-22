package ch.verno.common.db.dto.table.text;

import ch.verno.common.db.dto.base.BaseDto;
import ch.verno.lib.Language;
import ch.verno.publ.Publ;

import javax.annotation.Nonnull;

public class TextDto extends BaseDto {

  @Nonnull private String identifier;
  @Nonnull private Language language;
  @Nonnull private String text;

  public TextDto() {
    this.identifier = Publ.EMPTY_STRING;
    this.language = Language.DE;
    this.text = Publ.EMPTY_STRING;
  }

  public TextDto(@Nonnull String identifier,
                 @Nonnull Language language,
                 @Nonnull String text) {
    this.identifier = identifier;
    this.language = language;
    this.text = text;
  }

  @Nonnull
  public String getIdentifier() {
    return identifier;
  }

  public void setIdentifier(@Nonnull final String identifier) {
    this.identifier = identifier;
  }

  @Nonnull
  public Language getLanguage() {
    return language;
  }

  public void setLanguage(@Nonnull final Language language) {
    this.language = language;
  }

  @Nonnull
  public String getText() {
    return text;
  }

  public void setText(@Nonnull final String text) {
    this.text = text;
  }
}
