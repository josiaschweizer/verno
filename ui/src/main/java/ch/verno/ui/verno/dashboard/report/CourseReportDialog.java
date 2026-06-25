package ch.verno.ui.verno.dashboard.report;

import ch.verno.contract.dto.table.course.CourseDto;
import ch.verno.contract.dto.table.participant.ParticipantDto;
import ch.verno.lib.Lazy;
import ch.verno.lib.Publ;
import ch.verno.rpc.client.file.ReportClient;
import ch.verno.ui.base.components.dialog.VAAbstractDialog;
import ch.verno.ui.base.components.file.pdf.PdfPreview;
import com.google.inject.Injector;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.dom.Style;
import jakarta.annotation.Nonnull;

import java.util.Collection;
import java.util.List;

public class CourseReportDialog extends VAAbstractDialog {

  @Nonnull private final Lazy<ReportClient> reportClient;

  @Nonnull private final CourseDto currentCourse;
  @Nonnull private final List<ParticipantDto> participantsInCourse;

  @Nonnull private String reportToken;

  public CourseReportDialog(@Nonnull final Injector injector,
                            @Nonnull final CourseDto currentCourse,
                            @Nonnull final List<ParticipantDto> participantsInCourse) {
    this.reportClient = Lazy.of(() -> injector.getInstance(ReportClient.class));

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
  protected HorizontalLayout createContent() {
    final var preview = new PdfPreview(buildInlineUrl(reportToken));
    preview.applyDefaultStyle();

    final var layout = new HorizontalLayout(preview);
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
    reportToken = reportClient.get().generateCourseReport(currentCourse, participantsInCourse);
  }

  @Nonnull
  private Button createDownloadButton() {
    final var hidden = new Anchor(buildAttachmentUrl(reportToken), getTranslation("shared.download"));
    hidden.getElement().setAttribute("download", true);
    hidden.getStyle().setDisplay(Style.Display.NONE);
    add(hidden);

    final var downloadButton = new Button(getTranslation("shared.download"));
    downloadButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    downloadButton.addClickListener(e -> {
      hidden.getElement().callJsFunction("click");
      close();
    });
    return downloadButton;
  }

  private void deleteTempOnServer() {
    reportServerGate.deleteTempFile(reportToken);
  }

  @Nonnull
  private String buildInlineUrl(@Nonnull final String token) {
    return ApiUrl.TEMP_FILE_REPORT + Publ.SLASH + token + ApiUrl.DISPOSITION_INLINE;
  }

  @Nonnull
  private String buildAttachmentUrl(@Nonnull final String token) {
    return ApiUrl.TEMP_FILE_REPORT + Publ.SLASH + token + ApiUrl.DISPOSITION_ATTACHMENT;
  }
}