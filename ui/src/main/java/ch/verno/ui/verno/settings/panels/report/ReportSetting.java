package ch.verno.ui.verno.settings.panels.report;

import ch.verno.common.tenant.TenantContext;
import ch.verno.contract.dto.table.setting.TenantSettingDto;
import ch.verno.contract.dto.table.tenant.TenantDto;
import ch.verno.lib.Lazy;
import ch.verno.lib.Publ;
import ch.verno.lib.VernoConstants;
import ch.verno.rpc.client.setting.TenantSettingClient;
import ch.verno.rpc.properties.tenant.TenantProperties;
import ch.verno.ui.base.components.file.FileType;
import ch.verno.ui.base.components.notification.NotificationFactory;
import ch.verno.ui.client.file.FileApiClient;
import ch.verno.ui.lib.settings.VABaseSetting;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.VaadinServletRequest;
import com.vaadin.flow.server.streams.UploadHandler;
import jakarta.annotation.Nonnull;

import java.io.ByteArrayInputStream;
import java.util.Optional;

public class ReportSetting extends VABaseSetting<TenantSettingDto> {

  public static final String TITLE_KEY = "setting.report.settings";

  @Nonnull private final Lazy<FileApiClient> fileApiClient;
  @Nonnull private final Lazy<TenantProperties> tenantProperties;
  @Nonnull private final Lazy<TenantSettingClient> tenantSettingClient;

  @Inject
  public ReportSetting(@Nonnull final Injector injector) {
    super(injector, TITLE_KEY, true);

    this.tenantProperties = Lazy.of(() -> injector.getInstance(TenantProperties.class));
    this.tenantSettingClient = Lazy.of(() -> injector.getInstance(TenantSettingClient.class));
    this.fileApiClient = Lazy.of(() -> new FileApiClient(injector, "http://localhost:8080")); //todo erweitern um die url nicht zu hardcoden

    this.dto = tenantSettingClient.get().getCurrentOrDefaultTenantSetting();
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
    final var courseReportName = entryFactory.createTextField(
            TenantSettingDto::getCourseReportName,
            TenantSettingDto::setCourseReportName,
            binder,
            Optional.empty(),
            getTranslation("course.pdf.course.report.name")
    );

    final var courseReportFileUpload = entryFactory.createFileUploadEntry(createUploadHandler(), getTranslation("setting.custom.report.template"));
    courseReportFileUpload.setDropLabel(new Span(getTranslation("setting.upload.custom.report.template")));
    courseReportFileUpload.setWidthFull();
    courseReportFileUpload.setMaxFiles(1);
    courseReportFileUpload.setAcceptedFileTypes(FileType.HTML.getMimeType());
    courseReportFileUpload.addFileRemovedListener(event -> {
      final var courseReportTemplate = dto.getCourseReportTemplate();
      if (courseReportTemplate == null) {
        return;
      }

      fileApiClient.get().deleteReportTemplate(
              resolveTenantSlug(),
              courseReportTemplate
      );

      dto.setCourseReportTemplate(null);

      // we don't save the dto because then all changes would be saved and with this methode we can only remove the template without saving other changes that the user maybe made but doesn't want to save yet
      final var toUpdate = tenantSettingClient.get().getCurrentOrDefaultTenantSetting();
      toUpdate.setCourseReportTemplate(null);
      tenantSettingClient.get().saveTenantSetting(toUpdate);
    });

    final var templateId = dto.getCourseReportTemplate();
    if (templateId != null) {
      final var report = fileApiClient.get().getReportTemplate(resolveTenantSlug(), templateId);

      if (report != null && report.bytes().length > 0) {
        courseReportFileUpload.prefillUploadWithExistingFile(
                report.filename(),
                "text/html",
                report.size()
        );
      } else if (report != null) {
        NotificationFactory.showWarningNotification(getTranslation("setting.file.0.could.not.be.loaded.the.file.is.empty.please.upload.a.new.file", report.filename()));
      }
    }

    final var layout = new VerticalLayout(courseReportName, courseReportFileUpload);
    layout.setPadding(false);
    layout.setMargin(false);
    return layout;
  }

  @Nonnull
  private String resolveTenantSlug() {
    final var tenant = tenantProperties.get().resolveCurrentTenant();
    return tenant.map(TenantDto::slug).orElse(Publ.EMPTY_STRING);
  }

  @Nonnull
  private UploadHandler createUploadHandler() {
    return event -> {
      final var vaadinRequest = event.getRequest();

      if (vaadinRequest instanceof VaadinServletRequest servletRequest) {
        final var session = servletRequest.getHttpServletRequest().getSession(false);

        if (session != null) {
          final var tenantIdAttribute = session.getAttribute(VernoConstants.ATTR_TENANT_ID);

          if (tenantIdAttribute instanceof Long tenantId) {
            TenantContext.set(tenantId);
          }
        }
      }

      try {
        final var fileBytes = event.getInputStream().readAllBytes();
        if (fileBytes.length == 0) {
          NotificationFactory.showErrorNotification(getTranslation("setting.uploaded.file.is.empty.a.report.template.is.required.to.generate.course.reports.so.it.cannot.be.empty"));
          return;
        }

        final var fileStorageResponse = fileApiClient.get().uploadReportTemplate(
                resolveTenantSlug(),
                event.getFileName(),
                event.getContentType(),
                new ByteArrayInputStream(fileBytes),
                event.getFileSize()
        );

        dto.setCourseReportTemplate(fileStorageResponse != null ? fileStorageResponse.id() : Publ.ZERO_LONG);
        tenantSettingClient.get().saveTenantSetting(dto);
      } finally {
        TenantContext.clear();
      }
    };
  }

  @Nonnull
  @Override
  protected Class<TenantSettingDto> getBeanType() {
    return TenantSettingDto.class;
  }

  @Nonnull
  @Override
  protected TenantSettingDto createNewBeanInstance() {
    return TenantSettingDto.empty();
  }

  @Override
  protected void save() {
    if (binder.writeBeanIfValid(dto)) {
      tenantSettingClient.get().saveTenantSetting(dto);
      binder.setBean(dto);
    }
  }
}
