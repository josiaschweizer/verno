package ch.verno.ui.verno.dashboard.io.dialog.export;

import ch.verno.contract.gateway.ApiUrl;
import ch.verno.lib.Attributes;
import ch.verno.lib.Lazy;
import ch.verno.lib.Publ;
import ch.verno.rpc.client.file.CsvClient;
import ch.verno.rpc.client.file.TempFileClient;
import ch.verno.ui.base.components.dialog.VAAbstractDialog;
import ch.verno.ui.base.components.file.csv.CsvPreview;
import ch.verno.ui.verno.dashboard.io.widgets.ExportEntityConfig;
import com.google.inject.Injector;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.dom.Style;
import jakarta.annotation.Nonnull;
import org.jetbrains.annotations.NonNls;

import java.util.Collection;
import java.util.List;

public class ExportDialog<T> extends VAAbstractDialog {

  @NonNls public static final String CLICK_JS = "click";

  @Nonnull private final Lazy<CsvClient> csvClient;
  @Nonnull private final Lazy<TempFileClient> tempFileClient;

  @Nonnull private String fileToken;

  public ExportDialog(@Nonnull final Injector injector,
                      @Nonnull final ExportEntityConfig<T> config) {
    this.csvClient = Lazy.of(() -> injector.getInstance(CsvClient.class));
    this.tempFileClient = Lazy.of(() -> injector.getInstance(TempFileClient.class));

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
    final var preview = new CsvPreview(tempFileClient, buildInlineUrl(fileToken));

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


  private void generateCsvFile(@Nonnull final ExportEntityConfig<T> config) {
    final var fileDto = csvClient.get().parseRows(config.getFileName(), config.getRows());
    fileToken = tempFileClient.get().store(fileDto);
  }

  private void deleteTempOnServer() {
    tempFileClient.get().delete(fileToken);
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
