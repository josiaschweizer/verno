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

  public void prefillUploadWithExistingFile(@Nonnull final String fileName,
                                            @Nonnull final String contentType,
                                            final long sizeBytes) {
    this.getElement().executeJs("""
              const name = $0;
              const type = $1;
              const size = $2;
            
              // vaadin-upload file item model
              this.files = [{
                name,
                type,
                size,
                progress: 100,
                complete: true,
                error: false,
                uploading: false
              }];
            """, fileName, contentType, sizeBytes);
  }


}
