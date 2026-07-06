package ch.verno.ui.base.components.anchor.variants;

import ch.verno.lib.Lazy;
import ch.verno.lib.Publ;
import ch.verno.rpc.client.file.TempFileClient;
import ch.verno.ui.base.components.anchor.VAAnchor;
import ch.verno.ui.base.components.button.VAButton;
import ch.verno.ui.base.components.file.FileType;
import ch.verno.ui.i18n.TranslationHelper;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.server.streams.DownloadHandler;
import com.vaadin.flow.server.streams.DownloadResponse;
import com.vaadin.flow.shared.Registration;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.io.ByteArrayInputStream;
import java.util.UUID;

public class VAFileDownloadButton extends VAAnchor {

  @Nonnull private final Lazy<TempFileClient> tempFileClient;

  @Nonnull private final VAButton downloadButton;

  @Nullable private String fileToken;
  @Nullable private String fallbackFileName;

  @Inject
  public VAFileDownloadButton(@Nonnull final Injector injector) {
    this.tempFileClient = Lazy.of(() -> injector.getInstance(TempFileClient.class));

    this.downloadButton = new VAButton(injector.getInstance(TranslationHelper.class).getTranslation("shared.download"));
    downloadButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

    setHref(getDownloadHandler());

    removeAll();
    add(downloadButton);
  }

  @Nonnull
  private DownloadHandler getDownloadHandler() {
    return DownloadHandler.fromInputStream(event -> {
      final var file = tempFileClient.get().loadFile(requireToken());
      return new DownloadResponse(
              new ByteArrayInputStream(file.pdfBytes()),
              file.filename().isBlank() ? getFallbackFileName() : file.filename(),
              FileType.CSV.getMimeType(),
              file.pdfBytes().length);
    });
  }

  public void setFileToken(@Nonnull final String fileToken) {
    this.fileToken = fileToken;
  }

  public void setFallbackFileName(@Nullable final String fallbackFileName) {
    this.fallbackFileName = fallbackFileName;
  }

  @Nonnull
  public Registration addClickListener(@Nonnull final ComponentEventListener<ClickEvent<Button>> listener) {
    return downloadButton.addClickListener(listener);
  }

  @Nonnull
  private String requireToken() {
    if (fileToken == null) {
      throw new IllegalStateException("fileToken not set before download");
    }

    return fileToken;
  }

  @Nonnull
  private String getFallbackFileName() {
    if (fallbackFileName != null) {
      return fallbackFileName;
    }

    return UUID.randomUUID()
            .toString()
            .replace(Publ.MINUS, Publ.EMPTY_STRING)
            .substring(0, 16);
  }
}