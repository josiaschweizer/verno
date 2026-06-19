package ch.verno.ui.base.components.upload;

import ch.verno.common.gate.server.TempFileServerGate;
import ch.verno.lib.CssImportConstants;
import ch.verno.lib.Publ;
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
import org.jetbrains.annotations.NonNls;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.function.Consumer;

@CssImport(CssImportConstants.VA_FILE_UPLOAD_AREA)
public class VAFileUploadArea extends VerticalLayout {

  // TODO: FIX MAX FILE = 1 -> setMaxFiles throw RuntimeException => component should support more than 1 file (see https://josiaschweizer.youtrack.cloud/issue/verno-147/VAFileUploadArea-support-more-than-one-file)

  @NonNls private static final String DROP_AREA_CLASSNAME = "va-file-upload-drop";
  @NonNls private static final String UPLOAD_CLASSNAME = "va-file-upload";

  @NonNls public static final String FORMAT_SIZE_KB = "%.1f KB";
  @NonNls public static final String FORMAT_SIZE_MB = "%.1f MB";
  @NonNls public static final String FORMAT_SIZE_BYTES = " B";
  @NonNls public static final String SANITIZE_REGEX = "[^a-zA-Z0-9._-]";
  @NonNls public static final String UPLOAD_JS_FUNCTION = "this.files = []; this.requestContentUpdate && this.requestContentUpdate();";

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

    getStyle()
            .setMargin(VernoUtility.LUMO_ZERO)
            .setGap(VernoUtility.LUMO_ZERO)
            .setOverflow(Style.Overflow.HIDDEN);

    dropArea = new Div();
    dropArea.addClassName(DROP_AREA_CLASSNAME);

    upload = new Upload();
    upload.addClassName(UPLOAD_CLASSNAME);
    upload.setSizeFull();
    upload.setAutoUpload(true);
    upload.setDropAllowed(true);
    upload.setMaxFiles(1);
    upload.setUploadButton(dropArea);

    resetDropAreaContent();

    upload.setUploadHandler(event -> {
      final var ui = UI.getCurrent();

      deleteTempIfPresent();
      originalFileName = event.getFileName();

      final var bytes = readAllBytes(event.getInputStream());
      sizeBytes = bytes.length;

      final String stored;
      try {
        stored = tempFileServerGate.store(sanitizeFileName(originalFileName), bytes);
      } catch (Exception e) {
        ui.access(() -> resetUI(true));
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

    upload.addFileRejectedListener(e ->
            getUI().ifPresent(ui ->
                    ui.access(() ->
                            resetUI(true)
                    )
            )
    );
    upload.addFileRemovedListener(e ->
            getUI().ifPresent(ui ->
                    ui.access(() -> {
                      deleteTempIfPresent();
                      resetUI(true);
                    })
            )
    );

    addAndExpand(upload);
  }

  public void setAcceptedFileTypes(@Nonnull final String... types) {
    upload.setAcceptedFileTypes(types);
    resetDropAreaContent();
  }

  public void setMaxFiles(final int maxFiles) {
    if (maxFiles > 1){
      throw new RuntimeException("VAFileUploadArea only supports a maximum of 1 file for now (see https://josiaschweizer.youtrack.cloud/issue/verno-147/VAFileUploadArea-support-more-than-one-file)");
    }

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
    resetUI(false);
  }

  public void refreshUI() {
    resetDropAreaContent();
  }

  private void resetUI(final boolean fireRemoveListener) {
    tempToken = null;
    originalFileName = null;
    sizeBytes = 0;

    upload.getElement().executeJs(UPLOAD_JS_FUNCTION);

    resetDropAreaContent();

    if (fireRemoveListener && onFileRemoved != null) {
      onFileRemoved.run();
    }
  }

  private void resetDropAreaContent() {
    dropArea.removeAll();

    if (hasFile() && originalFileName != null) {
      dropArea.add(new Span(originalFileName));
      dropArea.add(new Span(formatSize(sizeBytes)));
      dropArea.add(new Span(
              getTranslation("base.klicken.oder.neue.datei.ziehen.zum.ersetzen")
      ));

      return;
    }

    dropArea.add(new Span(getTranslation("base.datei.hochladen")));
    dropArea.add(new Span(getTranslation("base.klicken.oder.datei.hierher.ziehen")));

    if (!upload.getAcceptedFileTypes().isEmpty()) {
      final var acceptedFileTypesText =
              String.join(Publ.COMMA + Publ.SPACE, upload.getAcceptedFileTypes());

      final var span = new Span();

      span.getElement().setProperty(
              "innerHTML",
              MessageFormat.format(
                      getTranslation("base.erlaubte.datentypen.0"),
                      "<i>" + acceptedFileTypesText + "</i>"
              )
      );

      dropArea.add(span);
    }
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

    return name.replaceAll(SANITIZE_REGEX, Publ.UNDERSCORE);
  }

  @Nonnull
  private String formatSize(final long bytes) {
    if (bytes < 1024) {
      return bytes + FORMAT_SIZE_BYTES;
    } else if (bytes < 1024 * 1024) {
      return String.format(FORMAT_SIZE_KB, bytes / 1024.0);
    }

    return String.format(FORMAT_SIZE_MB, bytes / (1024.0 * 1024));
  }
}