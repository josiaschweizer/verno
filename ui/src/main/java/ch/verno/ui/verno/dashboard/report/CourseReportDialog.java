package ch.verno.ui.verno.dashboard.report;

import ch.verno.common.db.dto.CourseDto;
import ch.verno.common.db.dto.ParticipantDto;
import ch.verno.common.report.ReportServerGate;
import ch.verno.common.report.ReportDto;
import ch.verno.publ.Publ;
import ch.verno.ui.base.dialog.VADialog;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.IFrame;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.server.streams.DownloadHandler;
import com.vaadin.flow.server.streams.DownloadResponse;
import jakarta.annotation.Nonnull;

import java.io.ByteArrayInputStream;
import java.util.Collection;
import java.util.List;

public class CourseReportDialog extends VADialog {

  @Nonnull private final ReportServerGate reportServerGate;
  @Nonnull private final CourseDto currentCourse;
  @Nonnull private final List<ParticipantDto> participantsInCourse;

  @Nonnull private ReportDto pdfDto;

  public CourseReportDialog(@Nonnull final ReportServerGate reportServerGate,
                            @Nonnull final CourseDto currentCourse,
                            @Nonnull final List<ParticipantDto> participantsInCourse) {
    this.reportServerGate = reportServerGate;
    this.currentCourse = currentCourse;
    this.participantsInCourse = participantsInCourse;

    generatePdf();
    initUI(getTranslation("shared.generate.report"));

    addDetachListener(e -> revokeBlobUrl());
    addDialogCloseActionListener(e -> revokeBlobUrl());
  }

  @Nonnull
  @Override
  protected HorizontalLayout createContent() {
    final byte[] pdfBytes = pdfDto.pdfBytes();

    final var previewFetchHandler = DownloadHandler
            .fromInputStream(event -> new DownloadResponse(
                    new ByteArrayInputStream(pdfBytes),
                    pdfDto.filename(),
                    "application/pdf",
                    pdfBytes.length
            ))
            .inline();

    final var preview = new IFrame();
    preview.setSizeFull();

    final var hidden = new Anchor(previewFetchHandler, Publ.EMPTY_STRING);
    hidden.getStyle().set("display", "none");
    add(hidden);

    preview.addAttachListener(e -> {
      preview.getElement().executeJs("""
                const iframe = this;
                const a = $0;
              
                if (window.__vernoPdfBlobUrl) {
                  URL.revokeObjectURL(window.__vernoPdfBlobUrl);
                  window.__vernoPdfBlobUrl = null;
                }
              
                const url = a.href;
                console.log("PDF fetch url:", url);
              
                fetch(url, { credentials: 'include' })
                  .then(r => {
                    if (!r.ok) {
                      throw new Error("HTTP " + r.status);
                    }
                    return r.blob();
                  })
                  .then(blob => {
                    const blobUrl = URL.createObjectURL(blob);
                    window.__vernoPdfBlobUrl = blobUrl;
                    iframe.src = blobUrl;
                    console.log("Blob URL set for iframe");
                  })
                  .catch(err => console.error("PDF blob fetch failed:", err));
              """, hidden.getElement());
    });

    final var layout = new HorizontalLayout(preview);
    layout.setSizeFull();
    layout.setPadding(false);
    layout.setSpacing(false);
    return layout;
  }

  @Nonnull
  @Override
  protected Collection<Button> createActionButtons() {
    final var cancelButton = new Button("Cancel", event -> close());
    final var downloadButton = createDownloadButton(pdfDto);
    return List.of(cancelButton, downloadButton);
  }

  private void generatePdf() {
    pdfDto = reportServerGate.generateCourseReportPdf(currentCourse, participantsInCourse);
  }

  @Nonnull
  private Button createDownloadButton(@Nonnull final ReportDto reportDto) {
    final var downloadHandler = DownloadHandler
            .fromInputStream(event -> new DownloadResponse(
                    new ByteArrayInputStream(reportDto.pdfBytes()),
                    reportDto.filename(),
                    "application/pdf",
                    reportDto.pdfBytes().length
            ));

    final var hidden = new Anchor(downloadHandler, getTranslation("shared.download"));
    hidden.getElement().setAttribute("download", true);
    hidden.getStyle().setDisplay(Style.Display.NONE);
    add(hidden);

    final var downloadButton = new Button(getTranslation("shared.download"));
    downloadButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    downloadButton.addClickListener(event -> {
      hidden.getElement().callJsFunction("click");
      close();
    });
    return downloadButton;
  }

  private void revokeBlobUrl() {
    UI ui = getUI().orElse(null);
    if (ui == null) {
      return;
    }
    ui.getPage().executeJs("""
              if (window.__vernoPdfBlobUrl) {
                URL.revokeObjectURL(window.__vernoPdfBlobUrl);
                window.__vernoPdfBlobUrl = null;
              }
            """);
  }
}