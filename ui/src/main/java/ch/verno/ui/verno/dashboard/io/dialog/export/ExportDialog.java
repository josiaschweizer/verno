package ch.verno.ui.verno.dashboard.io.dialog.export;

import ch.verno.common.file.FileServerGate;
import ch.verno.common.gate.GlobalInterface;
import ch.verno.common.server.ServerGate;
import ch.verno.publ.ApiUrl;
import ch.verno.publ.Publ;
import ch.verno.ui.base.dialog.VADialog;
import ch.verno.ui.base.file.csv.CsvPreview;
import ch.verno.ui.verno.dashboard.io.widgets.ExportEntityConfig;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.dom.Style;
import jakarta.annotation.Nonnull;

import java.util.Collection;
import java.util.List;

public class ExportDialog<T> extends VADialog {

  @Nonnull private final ServerGate serverGate;
  @Nonnull private final FileServerGate fileServerGate;
  @Nonnull private String fileToken;

  public ExportDialog(@Nonnull final GlobalInterface globalInterface,
                      @Nonnull final ExportEntityConfig<T> config) {
    this.serverGate = globalInterface.getGate(ServerGate.class);
    this.fileServerGate = globalInterface.getGate(FileServerGate.class);

    generateCsvFile(config);
    initUI(getTranslation("shared.export.csv"));

    setWidth("80%");
    setHeight("90%");

    addDetachListener(e -> deleteTempOnServer());
    addDialogCloseActionListener(e -> deleteTempOnServer());
  }

  @Nonnull
  @Override
  protected HorizontalLayout createContent() {
    final var preview = new CsvPreview(buildInlineUrl(fileToken));

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

  @Nonnull
  private Button createDownloadButton() {
    final var hidden = new Anchor(buildAttachmentUrl(fileToken), getTranslation("shared.download"));
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


  private void generateCsvFile(@Nonnull final ExportEntityConfig<T> config) {
    final var fileDto = serverGate.generateFileFromCsv(config.getFileName(), config.getRows());
    fileToken = fileServerGate.store(fileDto);
  }

  private void deleteTempOnServer() {
    fileServerGate.delete(fileToken);
  }

  @Nonnull
  private String buildInlineUrl(@Nonnull final String token) {
    return ApiUrl.TEMP_FILE_EXPORT + Publ.SLASH + token + ApiUrl.DISPOSITION_INLINE;
  }

  @Nonnull
  private String buildAttachmentUrl(@Nonnull final String token) {
    return ApiUrl.TEMP_FILE_EXPORT + Publ.SLASH + token + ApiUrl.DISPOSITION_ATTACHMENT;
  }
}
