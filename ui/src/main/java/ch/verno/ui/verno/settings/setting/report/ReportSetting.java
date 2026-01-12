package ch.verno.ui.verno.settings.setting.report;

import ch.verno.common.db.dto.MandantSettingDto;
import ch.verno.common.db.service.IMandantSettingService;
import ch.verno.ui.base.factory.EntryFactory;
import ch.verno.ui.base.settings.VABaseSetting;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.i18n.I18NProvider;
import com.vaadin.flow.server.VaadinService;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.Optional;

public class ReportSetting extends VABaseSetting<MandantSettingDto> {

  public static final String TITLE_KEY = "setting.report.settings";

  @Nonnull private final IMandantSettingService mandantSettingService;
  @Nonnull private final EntryFactory<MandantSettingDto> entryFactory;

  public ReportSetting(@Nonnull final IMandantSettingService mandantSettingService) {
    super(TITLE_KEY, true);

    this.mandantSettingService = mandantSettingService;
    this.dto = mandantSettingService.getSingleMandantSetting();
    this.entryFactory = new EntryFactory<>(getI18NProvider());
  }

  @Nonnull
  @Override
  protected Component createContent() {
    final var courseReport = entryFactory.createTextEntry(
            MandantSettingDto::getCourseReportName,
            MandantSettingDto::setCourseReportName,
            binder,
            Optional.empty(),
            "PDF Course Report Name"
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
      mandantSettingService.saveSingleMandantSetting(dto);
    }
  }

  @Nullable
  protected I18NProvider getI18NProvider() {
    final var service = VaadinService.getCurrent();
    if (service != null && service.getInstantiator() != null) {
      return service.getInstantiator().getI18NProvider();
    }
    return null;
  }
}
