package ch.verno.ui.verno.dashboard.io.dialog.importing.steps.error;

import ch.verno.common.lib.api.ApiUrl;
import ch.verno.lib.Publ;
import ch.verno.ui.base.components.dialog.DialogSize;
import ch.verno.ui.base.components.dialog.VAAbstractDialog;
import ch.verno.ui.base.components.layout.horizontal.VAHorizontalLayout;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.dom.Style;
import jakarta.annotation.Nonnull;
import org.jetbrains.annotations.NonNls;

import java.util.Collection;
import java.util.List;

public class ImportErrorDownloadDialog extends VAAbstractDialog {

  @NonNls public static final String DOWNLOAD_ATTRIBUTE = "download";
  @NonNls public static final String CLICK_JS = "click";

  @Nonnull private final String errorFileToken;
  @Nonnull private final String fileName;

  public ImportErrorDownloadDialog(@Nonnull final String errorFileToken,
                                   @Nonnull final String fileName) {
    this.errorFileToken = errorFileToken;
    this.fileName = fileName;

    initUI(getTranslation("shared.download"), DialogSize.MEDIUM);
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
    final var cancelButton = new Button(getTranslation("shared.cancel"), e -> close());
    final var downloadButton = createDownloadButton();
    return List.of(cancelButton, downloadButton);
  }

  @Nonnull
  private Button createDownloadButton() {
    final var hidden = new Anchor(buildAttachmentUrl(errorFileToken), fileName);
    hidden.getElement().setAttribute(DOWNLOAD_ATTRIBUTE, true);
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

  @Nonnull
  private String buildAttachmentUrl(@Nonnull final String token) {
    return ApiUrl.TEMP_FILE_IMPORT + Publ.SLASH + token + ApiUrl.DISPOSITION_ATTACHMENT;
  }
}