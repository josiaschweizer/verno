package ch.verno.ui.verno.settings.panels.gender;

import ch.verno.common.db.dto.base.BaseDto;
import ch.verno.common.db.dto.table.GenderDto;
import ch.verno.lib.New;
import jakarta.annotation.Nonnull;

import java.util.List;

public class GenderSettingDto extends BaseDto {

  @Nonnull private List<GenderDto> genders;

  public GenderSettingDto(@Nonnull List<GenderDto> genders) {
    this.genders = genders;
  }

  @Nonnull
  public static GenderSettingDto empty() {
    return new GenderSettingDto(New.arrayList());
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
}
