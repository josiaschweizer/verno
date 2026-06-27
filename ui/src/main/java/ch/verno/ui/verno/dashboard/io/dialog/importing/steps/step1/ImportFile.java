package ch.verno.ui.verno.dashboard.io.dialog.importing.steps.step1;

import ch.verno.lib.VernoUtility;
import ch.verno.ui.base.components.dialog.stepdialog.BaseDialogStep;
import ch.verno.ui.base.components.file.FileType;
import ch.verno.ui.base.components.upload.VAFileUploadArea;
import com.google.inject.Inject;
import com.google.inject.Injector;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public class ImportFile extends BaseDialogStep {

  private final VAFileUploadArea fileUpload;

  @Inject
  public ImportFile(@Nonnull final Injector injector) {
    setSizeFull();
    setPadding(false);
    setSpacing(false);
    getStyle().setMargin(VernoUtility.NONE).setGap(VernoUtility.NONE);

    fileUpload = injector.getInstance(VAFileUploadArea.class);
    fileUpload.setAcceptedFileTypes(FileType.CSV.getFileExtension());
    fileUpload.setMaxFiles(1);

    addAndExpand(fileUpload);
  }

  public void setOnFileUploadedListener(@Nullable final Runnable listener) {
    if (listener == null) {
      fileUpload.addFileUploadedListener(token -> {
      });
    } else {
      fileUpload.addFileUploadedListener(token -> listener.run());
    }
  }

  public boolean hasFile() {
    return fileUpload.hasFile();
  }

  @Nullable
  public String getTempToken() {
    return fileUpload.getTempToken();
  }

  public void cleanup() {
    fileUpload.cleanup();
  }

  @Override
  public boolean isValid() {
    return hasFile();
  }

  @Override
  public void onBecomeVisible() {
    fileUpload.refreshUI();
  }
}