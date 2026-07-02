package ch.verno.ui.verno.dashboard.report;

import ch.verno.common.lib.api.ApiQueryParam;
import ch.verno.common.lib.api.ApiUrl;
import ch.verno.common.lib.url.UrlBuilder;
import ch.verno.contract.dto.table.course.CourseDto;
import ch.verno.contract.dto.table.participant.ParticipantDto;
import ch.verno.lib.Attributes;
import ch.verno.lib.Lazy;
import ch.verno.lib.Publ;
import ch.verno.rpc.client.file.ReportClient;
import ch.verno.rpc.properties.api.ApiConfigProperties;
import ch.verno.ui.base.components.dialog.VAAbstractDialog;
import ch.verno.ui.base.components.file.pdf.PdfPreview;
import ch.verno.ui.base.components.layout.horizontal.VAHorizontalLayout;
import com.google.inject.Injector;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.dom.Style;
import jakarta.annotation.Nonnull;
import org.jetbrains.annotations.NonNls;

import java.util.Collection;
import java.util.List;

public class CourseReportDialog extends VAAbstractDialog {

  @NonNls public static final String CLICK_JS = "click";

  @Nonnull private final Lazy<ReportClient> reportClient;
  @Nonnull private final Lazy<ApiConfigProperties> apiConfigProperties;

  @Nonnull private final CourseDto currentCourse;
  @Nonnull private final List<ParticipantDto> participantsInCourse;

  @Nonnull private String reportToken;
  @Nonnull private String apiAccessToken;

  public CourseReportDialog(@Nonnull final Injector injector,
                            @Nonnull final CourseDto currentCourse,
                            @Nonnull final List<ParticipantDto> participantsInCourse) {
    this.reportClient = Lazy.of(() -> injector.getInstance(ReportClient.class));
    this.apiConfigProperties = Lazy.of(() -> injector.getInstance(ApiConfigProperties.class));

    this.currentCourse = currentCourse;
    this.participantsInCourse = participantsInCourse;

    generateReport();
    initUI(getTranslation("shared.generate.report"));

    setWidth("80%");
    setHeight("90%");

    addDetachListener(e -> deleteTempOnServer());
    addDialogCloseActionListener(e -> deleteTempOnServer());
  }

  @Nonnull
  @Override
  protected VAHorizontalLayout createContent() {
    final var preview = new PdfPreview(buildInlineUrl(reportToken));
    preview.applyDefaultStyle();

    final var layout = new VAHorizontalLayout(preview);
    layout.setSizeFull();
    layout.setPadding(false);
    layout.setSpacing(false);
    return layout;
  }

  @Nonnull
  @Override
  protected Collection<Button> createActionButtons() {
    final var cancelButton = new Button(getTranslation("shared.cancel"), e -> close());
    final var downloadButton = createDownloadButton();
    
    return List.of(cancelButton, downloadButton);
  }

  private void generateReport() {
    this.reportToken = reportClient.get().generateCourseReport(currentCourse, participantsInCourse);
    this.apiAccessToken = reportClient.get().issueAccessToken(reportToken);
  }

  @Nonnull
  private Button createDownloadButton() {
    final var hidden = new Anchor(buildAttachmentUrl(reportToken), getTranslation("shared.download"));
    hidden.getElement().setAttribute(Attributes.DOWNLOAD, true);
    hidden.getStyle().setDisplay(Style.Display.NONE);
    add(hidden);

    final var downloadButton = new Button(getTranslation("shared.download"));
    downloadButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    downloadButton.addClickListener(e -> {
      hidden.getElement().callJsFunction(CLICK_JS);
      close();
    });
    return downloadButton;
  }

  private void deleteTempOnServer() {
    reportClient.get().deleteTempFile(reportToken);
  }

  @Nonnull
  private String buildInlineUrl(@Nonnull final String token) {
    final var path = ApiUrl.TEMP_FILE_REPORT_PUBLIC + Publ.SLASH + token;
    return buildUrl(path, ApiQueryParam.DISPOSITION_INLINE);
  }

  @Nonnull
  private String buildAttachmentUrl(@Nonnull final String token) {
    final var path = ApiUrl.TEMP_FILE_REPORT_PUBLIC + Publ.SLASH + token;
    return buildUrl(path, ApiQueryParam.DISPOSITION_ATTACHMENT);
  }

  @Nonnull
  private String buildUrl(@Nonnull final String path, @Nonnull final String disposition) {
    final var baseUrl = apiConfigProperties.get().getBaseUrl();

    return UrlBuilder.of(baseUrl, path)
            .withDisposition(disposition)
            .withAccessToken(apiAccessToken)
            .build();
  }
}