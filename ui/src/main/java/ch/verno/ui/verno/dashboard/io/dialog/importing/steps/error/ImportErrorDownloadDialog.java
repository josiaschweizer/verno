package ch.verno.ui.verno.dashboard.io.dialog.importing.steps.error;

import ch.verno.lib.New;
import ch.verno.ui.base.components.anchor.variants.VAFileDownloadButton;
import ch.verno.ui.base.components.dialog.DialogSize;
import ch.verno.ui.base.components.dialog.VAAbstractDialog;
import ch.verno.ui.base.components.layout.horizontal.VAHorizontalLayout;
import com.google.inject.Injector;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.Collection;

public class ImportErrorDownloadDialog extends VAAbstractDialog {

  @Nonnull private final Injector injector;
  @Nonnull private final String fileToken;

  public ImportErrorDownloadDialog(@Nonnull final Injector injector,
                                   @Nonnull final String fileToken) {
    this.injector = injector;
    this.fileToken = fileToken;

    initUI(getTranslation("shared.download"), DialogSize.MEDIUM);
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
    final var text = new Text(getTranslation("shared.beim.import.konnten.nicht.alle.datensatze.erfolgreich.verarbeitet.werden.die.betroffenen.eintrage.wurden.in.einer.separaten.datei.zusammengefasst.und.konnen.hier.heruntergeladen.werden"));
    final var layout = new VAHorizontalLayout(text);
    layout.setWidthFull();
    layout.setPadding(false);
    layout.setSpacing(false);
    return layout;
  }

  @Nonnull
  @Override
  protected Collection<Button> createActionButtons() {
    // return empty list because action buttons gets added by overridden initUI directly onto the footer
    return New.list();
  }

  @Nonnull
  private VAFileDownloadButton createDownloadButton() {
    final var downloadButton = injector.getInstance(VAFileDownloadButton.class);
    downloadButton.setFileToken(fileToken);
    downloadButton.setFallbackFileName("import_errors.csv");
    downloadButton.addClickListener(this::close);
    return downloadButton;
  }

}