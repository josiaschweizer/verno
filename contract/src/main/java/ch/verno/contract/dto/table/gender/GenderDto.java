package ch.verno.contract.dto.table.gender;

import ch.verno.contract.dto.table.base.BaseDto;
import ch.verno.contract.dto.table.text.TextDto;
import ch.verno.lib.Publ;
import ch.verno.lib.lib.language.Language;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.Map;
import java.util.Objects;

public class GenderDto extends BaseDto {

  @Nonnull private String name;
  @Nonnull private String description;
  @Nullable private Map<Language, TextDto> userDisplayTexts;

  private GenderDto() {
    setId(null);
    this.name = Publ.EMPTY_STRING;
    this.description = Publ.EMPTY_STRING;
    this.userDisplayTexts = null;
  }

  public GenderDto(@Nullable final Long id,
                   @Nonnull final String name,
                   @Nonnull final String description,
                   @Nullable final Map<Language, TextDto> userDisplayTexts) {
    setId(id);
    this.name = name;
    this.description = description;
    this.userDisplayTexts = userDisplayTexts;
  }

  @Nonnull
  public static GenderDto empty() {
    return new GenderDto();
  }

  @Nonnull
  public static GenderDto ref(@Nonnull final Long id) {
    final var dto = new GenderDto();
    dto.setId(id);
    return dto;
  }

  public boolean isEmpty() {
    return getId() != null
            && getId() == 0L
            && name.isEmpty();
  }

  @Nonnull
  public String getName() {
    return name;
  }

  public void setName(@Nonnull final String name) {
    this.name = name;
  }

  @Nonnull
  public String getDescription() {
    return description;
  }

  public void setDescription(@Nonnull final String description) {
    this.description = description;
  }

  @Nullable
  public Map<Language, TextDto> getUserDisplayTexts() {
    return userDisplayTexts;
  }

  public void setUserDisplayTexts(@Nullable final Map<Language, TextDto> userDisplayTexts) {
    this.userDisplayTexts = userDisplayTexts;
  }

  @Nonnull
  public String getTranslation(@Nonnull final Language language) {
    final var textDto = userDisplayTexts.get(language);
    if (textDto == null) {
      return description; // use description as fallback
    }
    return textDto.getText();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof GenderDto other)) return false;
    return getId() != null && getId().equals(other.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId());
  }
}