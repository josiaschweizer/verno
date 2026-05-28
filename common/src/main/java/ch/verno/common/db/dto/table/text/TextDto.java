package ch.verno.common.db.dto.table.text;

import ch.verno.common.db.dto.base.BaseDto;
import ch.verno.lib.language.Language;
import ch.verno.publ.Publ;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TextDto extends BaseDto {

  @Nonnull private String identifier;
  @Nullable private String subIdentifier;
  @Nonnull private Language language;
  @Nonnull private String text;

  private TextDto() {
    this.identifier = Publ.EMPTY_STRING;
    this.subIdentifier = null;
    this.language = Language.DE;
    this.text = Publ.EMPTY_STRING;
  }

  public TextDto(@Nonnull String identifier,
                 @Nonnull String subIdentifier,
                 @Nonnull Language language,
                 @Nonnull String text) {
    this.identifier = identifier;
    this.subIdentifier = subIdentifier;
    this.language = language;
    this.text = text;
  }

  @Nonnull
  public static TextDto empty(){
    return new TextDto();
  }

  @Nonnull
  public String getIdentifier() {
    return identifier;
  }

  public void setIdentifier(@Nonnull final String identifier) {
    this.identifier = identifier;
  }

  @Nullable
  public String getSubIdentifier() {
    return subIdentifier;
  }

  public void setSubIdentifier(@Nullable final String subIdentifier) {
    this.subIdentifier = subIdentifier;
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
