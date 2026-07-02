package ch.verno.ui.base.report;

import ch.verno.lib.New;
import ch.verno.ui.base.components.dialog.VAAbstractDialog;
import com.vaadin.flow.component.button.Button;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.Collection;

public abstract class BaseReportPreviewDialog extends VAAbstractDialog {

  @Nullable private String reportToken;
  @Nullable private String apiAccessToken;

  @Nonnull
  @Override
  protected Collection<Button> createActionButtons() {
    final var cancelButton = new Button(getTranslation("shared.cancel"), e -> close());
//    final var downloadButton = createDownloadButton();

//    return List.of(cancelButton, downloadButton);
    return New.list();
  }

  protected abstract String getApiUrl();
}
