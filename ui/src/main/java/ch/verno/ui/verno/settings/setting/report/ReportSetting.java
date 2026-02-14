package ch.verno.ui.verno.settings.setting.report;

import ch.verno.common.db.dto.table.TenantSettingDto;
import ch.verno.common.db.service.ITenantSettingService;
import ch.verno.common.gate.servergate.TempFileServerGate;
import ch.verno.common.gate.GlobalInterface;
import ch.verno.ui.base.components.file.FileType;
import ch.verno.ui.base.factory.EntryFactory;
import ch.verno.ui.base.settings.VABaseSetting;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.streams.UploadEvent;
import com.vaadin.flow.server.streams.UploadHandler;
import jakarta.annotation.Nonnull;

import java.io.IOException;
import java.util.Optional;

public class ReportSetting extends VABaseSetting<TenantSettingDto> {

  public static final String TITLE_KEY = "setting.report.settings";

  @Nonnull private final ITenantSettingService tenantSettingService;
  @Nonnull private final EntryFactory<TenantSettingDto> entryFactory;

  public ReportSetting(@Nonnull final GlobalInterface globalInterface) {
    super(globalInterface, TITLE_KEY, true);

    this.tenantSettingService = globalInterface.getService(ITenantSettingService.class);
    this.dto = tenantSettingService.getCurrentTenantSettingOrDefault();
    this.entryFactory = new EntryFactory<>(globalInterface.getI18NProvider());
  }

  @Nonnull
  @Override
  protected Component createContent() {
    final var courseLayout = createCourseLayout();

    final var contentLayout = new VerticalLayout(courseLayout);
    contentLayout.setPadding(false);
    contentLayout.setMargin(false);
    return contentLayout;
  }

  @Nonnull
  private VerticalLayout createCourseLayout() {
    final var courseReportName = entryFactory.createTextEntry(
            TenantSettingDto::getCourseReportName,
            TenantSettingDto::setCourseReportName,
            binder,
            Optional.empty(),
            getTranslation("course.pdf.course.report.name")
    );

    final var uploadHandler = new UploadHandler() {

      @Override
      public void handleUploadRequest(@Nonnull final UploadEvent event) throws IOException {
        final var fileServerGate = globalInterface.getGate(TempFileServerGate.class);
        System.out.println(event);
      }

    };
    final var courseReportFileUpload = entryFactory.createFileUploadEntry(uploadHandler, "Custom Report Template");
    courseReportFileUpload.setWidthFull();
    courseReportFileUpload.setMaxFiles(1);
    courseReportFileUpload.setAcceptedFileTypes(FileType.HTML.getMimeType());

    return new VerticalLayout(courseReportName, courseReportFileUpload);
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
