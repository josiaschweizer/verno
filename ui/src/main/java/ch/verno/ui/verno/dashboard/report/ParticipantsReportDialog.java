package ch.verno.ui.verno.dashboard.report;

import ch.verno.common.report.ReportServerGate;
import ch.verno.publ.ApiUrl;
import ch.verno.publ.Publ;
import ch.verno.ui.base.dialog.VADialog;
import ch.verno.ui.base.pdf.PdfPreview;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.dom.Style;
import jakarta.annotation.Nonnull;

import java.util.Collection;
import java.util.List;

public class ParticipantsReportDialog extends VADialog {

  @Nonnull private final ReportServerGate reportServerGate;

  @Nonnull private String reportToken;

  public ParticipantsReportDialog(@Nonnull final ReportServerGate reportServerGate) {
    this.reportServerGate = reportServerGate;

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
    reportToken = reportServerGate.generateParticipantsReport();
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