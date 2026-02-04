package ch.verno.ui.verno.settings.setting.report;

import ch.verno.common.db.dto.table.MandantSettingDto;
import ch.verno.common.db.service.IMandantSettingService;
import ch.verno.common.lib.i18n.TranslationHelper;
import ch.verno.ui.base.factory.EntryFactory;
import ch.verno.ui.base.settings.VABaseSetting;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import jakarta.annotation.Nonnull;

import java.util.Optional;

public class ReportSetting extends VABaseSetting<MandantSettingDto> {

  public static final String TITLE_KEY = "setting.report.settings";

  @Nonnull private final IMandantSettingService mandantSettingService;
  @Nonnull private final EntryFactory<MandantSettingDto> entryFactory;

  public ReportSetting(@Nonnull final IMandantSettingService mandantSettingService) {
    super(TITLE_KEY, true);

    this.mandantSettingService = mandantSettingService;
    this.dto = mandantSettingService.getCurrentMandantSettingOrDefault();
    this.entryFactory = new EntryFactory<>(TranslationHelper.getI18NProvider());
  }

  @Nonnull
  @Override
  protected Component createContent() {
    final var courseReport = entryFactory.createTextEntry(
            MandantSettingDto::getCourseReportName,
            MandantSettingDto::setCourseReportName,
            binder,
            Optional.empty(),
            getTranslation("course.pdf.course.report.name")
    );

    final var contentLayout = new VerticalLayout(courseReport);
    contentLayout.setPadding(false);
    contentLayout.setMargin(false);
    return contentLayout;
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

  @Override
  protected void save() {
    if (binder.writeBeanIfValid(dto)) {
      mandantSettingService.saveCurrentMandantSetting(dto);
    }
  }
}
