package ch.verno.ui.verno.dashboard.io.dialog.importing.steps.error;

import ch.verno.publ.ApiUrl;
import ch.verno.publ.Publ;
import ch.verno.ui.base.dialog.DialogSize;
import ch.verno.ui.base.dialog.VADialog;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.dom.Style;
import jakarta.annotation.Nonnull;

import java.util.Collection;
import java.util.List;

public class ImportErrorDownloadDialog extends VADialog {

  @Nonnull private final String errorFileToken;
  @Nonnull private final String fileName;

  public ImportErrorDownloadDialog(@Nonnull final String errorFileToken,
                                   @Nonnull final String fileName) {
    this.errorFileToken = errorFileToken;
    this.fileName = fileName;

    initUI(getTranslation("shared.download"), DialogSize.SMALL);
  }

  @Nonnull
  @Override
  protected HorizontalLayout createContent() {
    final var text = new Text("Beim Import konnten nicht alle Datensätze erfolgreich verarbeitet werden. Die betroffenen Einträge wurden in einer separaten Datei zusammengefasst und können hier heruntergeladen werden.");
    final var layout = new HorizontalLayout(text);
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

  @Nonnull
  private String buildAttachmentUrl(@Nonnull final String token) {
    return ApiUrl.TEMP_FILE_IMPORT + Publ.SLASH + token + ApiUrl.DISPOSITION_ATTACHMENT;
  }
}