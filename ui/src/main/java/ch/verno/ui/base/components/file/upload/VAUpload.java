package ch.verno.ui.base.components.file.upload;

import ch.verno.publ.Publ;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.server.streams.UploadHandler;
import jakarta.annotation.Nonnull;

public class VAUpload extends Upload {

  public VAUpload(@Nonnull final UploadHandler uploadHandler) {
    this(uploadHandler, Publ.EMPTY_STRING);
  }

  public VAUpload(@Nonnull final UploadHandler uploadHandler,
                  @Nonnull final String targetName) {
    super(uploadHandler, targetName);
  }

}
