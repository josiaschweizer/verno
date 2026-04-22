package ch.verno.ui.verno.dashboard.io.dialog.importing.steps.step1;

import ch.verno.common.gate.GlobalInterface;
import ch.verno.common.gate.server.TempFileServerGate;
import ch.verno.ui.base.components.dialog.stepdialog.BaseDialogStep;
import ch.verno.ui.base.components.upload.VAFileUploadArea;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public class ImportFile extends BaseDialogStep {

  private final VAFileUploadArea fileUpload;

  public ImportFile(@Nonnull final GlobalInterface globalInterface) {
    setSizeFull();
    setPadding(false);
    setSpacing(false);
    getStyle().setMargin("0")
            .setGap("0");

    fileUpload = new VAFileUploadArea(globalInterface.getService(TempFileServerGate.class));
    fileUpload.setAcceptedFileTypes(".csv");
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