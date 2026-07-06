package ch.verno.ui.verno.dashboard.io.dialog.export;

import ch.verno.lib.Lazy;
import ch.verno.lib.New;
import ch.verno.rpc.client.file.CsvClient;
import ch.verno.rpc.client.file.TempFileClient;
import ch.verno.rpc.properties.application.ApplicationProperties;
import ch.verno.ui.base.components.anchor.VAAnchor;
import ch.verno.ui.base.components.anchor.variants.VAFileDownloadButton;
import ch.verno.ui.base.components.dialog.DialogSize;
import ch.verno.ui.base.components.dialog.VAAbstractDialog;
import ch.verno.ui.base.components.file.csv.CsvPreview;
import ch.verno.ui.base.components.layout.horizontal.VAHorizontalLayout;
import ch.verno.ui.verno.dashboard.io.widgets.ExportEntityConfig;
import com.google.inject.Injector;
import com.vaadin.flow.component.button.Button;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.jetbrains.annotations.NonNls;

import java.util.Collection;

public class ExportDialog<T> extends VAAbstractDialog {

  @NonNls public static final String CLICK_JS = "click";

  @Nonnull private final Injector injector;
  @Nonnull private final Lazy<CsvClient> csvClient;
  @Nonnull private final Lazy<TempFileClient> tempFileClient;
  @Nonnull private final Lazy<ApplicationProperties> applicationProperties;

  @Nonnull private String fileToken;

  public ExportDialog(@Nonnull final Injector injector,
                      @Nonnull final ExportEntityConfig<T> config) {
    this.injector = injector;
    this.csvClient = Lazy.of(() -> injector.getInstance(CsvClient.class));
    this.tempFileClient = Lazy.of(() -> injector.getInstance(TempFileClient.class));
    this.applicationProperties = Lazy.of(() -> injector.getInstance(ApplicationProperties.class));

    generateCsvFile(config);
    initUI(getTranslation("shared.export.csv"));

    setWidth("80%");
    setHeight("90%");

    addDetachListener(e -> deleteTempOnServer());
    addDialogCloseActionListener(e -> deleteTempOnServer());
  }

  @Override
  protected void initUI(@Nullable final String title, @Nonnull final DialogSize dialogSize) {
    super.initUI(title, dialogSize);

    getFooter().removeAll();
    getFooter().add(createCancelButton());
    getFooter().add(createDownloadButton());
  }

  @Nonnull
  @Override
  protected VAHorizontalLayout createContent() {
    final var preview = new CsvPreview(tempFileClient, fileToken);

    final var layout = new VAHorizontalLayout(preview);
    layout.setSizeFull();
    layout.setPadding(false);
    layout.setSpacing(false);
    return layout;
  }

  @Nonnull
  @Override
  protected Collection<Button> createActionButtons() {
    // return empty collection because we override the initUI and add there the download anchor to the footer
    return New.list();
  }


  @Nonnull
  private VAAnchor createDownloadButton() {
    final var fileDownloadButton = injector.getInstance(VAFileDownloadButton.class);
    fileDownloadButton.addClickListener(e -> close());
    fileDownloadButton.setFileToken(fileToken);
    return fileDownloadButton;
  }

  private void generateCsvFile(@Nonnull final ExportEntityConfig<T> config) {
    final var fileDto = csvClient.get().parseCsvRowsToFile(config.getFileName(), config.getRows());
    this.fileToken = tempFileClient.get().store(fileDto);
  }

  private void deleteTempOnServer() {
    tempFileClient.get().delete(fileToken);
  }
}
