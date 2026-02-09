package ch.verno.ui.verno.settings.setting.report;

import ch.verno.common.db.dto.table.TenantSettingDto;
import ch.verno.common.db.service.ITenantSettingService;
import ch.verno.common.gate.GlobalInterface;
import ch.verno.ui.base.factory.EntryFactory;
import ch.verno.ui.base.settings.VABaseSetting;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import jakarta.annotation.Nonnull;

import java.util.Optional;

public class ReportSetting extends VABaseSetting<TenantSettingDto> {

  public static final String TITLE_KEY = "setting.report.settings";

  @Nonnull private final ITenantSettingService tenantSettingService;
  @Nonnull private final EntryFactory<TenantSettingDto> entryFactory;

  public ReportSetting(@Nonnull final GlobalInterface globalInterface) {
    super(TITLE_KEY, true);

    this.tenantSettingService = globalInterface.getService(ITenantSettingService.class);
    this.dto = tenantSettingService.getCurrentTenantSettingOrDefault();
    this.entryFactory = new EntryFactory<>(globalInterface.getI18NProvider());
  }

  @Nonnull
  @Override
  protected Component createContent() {
    final var courseReport = entryFactory.createTextEntry(
            TenantSettingDto::getCourseReportName,
            TenantSettingDto::setCourseReportName,
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
  protected Class<TenantSettingDto> getBeanType() {
    return TenantSettingDto.class;
  }

  @Nonnull
  @Override
  protected TenantSettingDto createNewBeanInstance() {
    return new TenantSettingDto();
  }

  @Override
  protected void save() {
    if (binder.writeBeanIfValid(dto)) {
      tenantSettingService.saveCurrentTenantSetting(dto);
    }
  }
}
