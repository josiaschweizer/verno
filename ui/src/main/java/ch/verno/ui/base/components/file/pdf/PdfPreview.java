package ch.verno.ui.base.components.file.pdf;

import ch.verno.ui.base.components.file.AbstractFilePreview;
import ch.verno.ui.base.components.file.FileType;
import jakarta.annotation.Nonnull;

public class PdfPreview extends AbstractFilePreview {

  public PdfPreview(@Nonnull final String src) {
    super(FileType.PDF);
    init();
    setSrc(src);
  }
}
