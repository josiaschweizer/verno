package ch.verno.ui.verno.settings.setting.report;

import ch.verno.common.db.dto.MandantSettingDto;
import ch.verno.ui.base.settings.VABaseSetting;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.data.binder.Binder;
import jakarta.annotation.Nonnull;

public class ReportSetting extends VABaseSetting<MandantSettingDto> {

  public static final String TITLE_KEY = "setting.report.settings";

  public ReportSetting() {
    super(TITLE_KEY, false);
  }

  @Nonnull
  @Override
  protected Component createContent() {
    return null;
  }

  @Nonnull
  @Override
  protected Binder<MandantSettingDto> createBinder() {
    return new Binder<>(MandantSettingDto.class);
  }

  @Nonnull
  @Override
  protected MandantSettingDto createNewBeanInstance() {
    return new MandantSettingDto();
  }
}
