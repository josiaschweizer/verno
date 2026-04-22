package ch.verno.ui.base.components.upload;

import ch.verno.common.gate.server.TempFileServerGate;
import ch.verno.publ.VernoUtility;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.dom.Style;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.function.Consumer;

@CssImport("./components/upload/va-file-upload-area.css")
public class VAFileUploadArea extends VerticalLayout {

  @Nonnull
  private final TempFileServerGate tempFileServerGate;

  @Nullable private String tempToken;
  @Nullable private String originalFileName;
  private long sizeBytes;

  @Nonnull private final Div dropArea;
  @Nonnull private final Upload upload;

  @Nullable private Consumer<String> onFileUploaded;
  @Nullable private Runnable onFileRemoved;

  public VAFileUploadArea(@Nonnull final TempFileServerGate tempFileServerGate) {
    this.tempFileServerGate = tempFileServerGate;

    setSizeFull();
    setPadding(false);
    setSpacing(false);
    getStyle().setMargin(VernoUtility.LUMO_ZERO)
            .setGap(VernoUtility.LUMO_ZERO)
            .setOverflow(Style.Overflow.HIDDEN);

    dropArea = new Div();
    dropArea.addClassName("va-file-upload__drop");
    resetDropAreaContent();

    upload = new Upload();
    upload.addClassName("va-file-upload");
    upload.setSizeFull();
    upload.setAutoUpload(true);
    upload.setDropAllowed(true);
    upload.setMaxFiles(1);
    upload.setUploadButton(dropArea);

    upload.setUploadHandler(event -> {
      final var ui = UI.getCurrent();

      deleteTempIfPresent();

      originalFileName = event.getFileName();
      final byte[] bytes = readAllBytes(event.getInputStream());
      sizeBytes = bytes.length;

      final String stored;
      try {
        stored = tempFileServerGate.store(sanitizeFileName(originalFileName), bytes);
      } catch (Exception e) {
        ui.access(this::resetUI);
        return;
      }

      tempToken = stored;

      getUI().ifPresent(newUi ->
              newUi.access(() -> {
                refreshUI();
                if (onFileUploaded != null && tempToken != null) {
                  onFileUploaded.accept(tempToken);
                }
              })
      );
    });
    upload.addFileRejectedListener(e -> getUI().ifPresent(ui -> ui.access(this::resetUI)));

    addAndExpand(upload);
  }

  public void setAcceptedFileTypes(@Nonnull final String... types) {
    upload.setAcceptedFileTypes(types);
  }

  public void setMaxFiles(final int maxFiles) {
    upload.setMaxFiles(maxFiles);
  }

  public void setMaxFileSizeBytes(final long maxBytes) {
    upload.setMaxFileSize((int) maxBytes);
  }

  public void addFileUploadedListener(@Nonnull final Consumer<String> listener) {
    this.onFileUploaded = listener;
  }

  public void addFileRemovedListener(@Nonnull final Runnable listener) {
    this.onFileRemoved = listener;
  }

  public boolean hasFile() {
    return tempToken != null && !tempToken.isBlank();
  }

  @Nullable
  public String getTempToken() {
    return tempToken;
  }

  public void cleanup() {
    deleteTempIfPresent();
    resetUI();
  }

  public void refreshUI() {
    if (hasFile() && originalFileName != null) {
      resetDropAreaContent();
    } else {
      resetDropAreaContent();
    }
  }

  private void resetUI() {
    tempToken = null;
    originalFileName = null;
    sizeBytes = 0;
    resetDropAreaContent();

    if (onFileRemoved != null) {
      onFileRemoved.run();
    }
  }

  private void resetDropAreaContent() {
    dropArea.removeAll();
    dropArea.add(new Span(getTranslation("base.datei.ausgewahlt")));
    dropArea.add(new Span(getTranslation("base.klicken.oder.neue.datei.ziehen.zum.ersetzen")));
  }

  private void deleteTempIfPresent() {
    if (tempToken == null) {
      return;
    }

    try {
      tempFileServerGate.delete(tempToken);
    } catch (Exception ignored) {
    } finally {
      tempToken = null;
    }
  }

  @Nonnull
  private byte[] readAllBytes(@Nonnull final InputStream in) {
    try (in; var out = new ByteArrayOutputStream()) {
      in.transferTo(out);
      return out.toByteArray();
    } catch (Exception e) {
      throw new IllegalStateException("Upload failed", e);
    }
  }

  @Nonnull
  private String sanitizeFileName(@Nullable final String name) {
    if (name == null || name.isBlank()) {
      return "upload.bin";
    }
    return name.replaceAll("[^a-zA-Z0-9._-]", "_");
  }

  @Nonnull
  private String formatSize(final long bytes) {
    if (bytes < 1024) {
      return bytes + " B";
    } else if (bytes < 1024 * 1024) {
      return String.format("%.1f KB", bytes / 1024.0);
    }
    return String.format("%.1f MB", bytes / (1024.0 * 1024));
  }
}