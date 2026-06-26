package ch.verno.server.bo.file;

import ch.verno.contract.dto.file.temp.FileDto;
import ch.verno.lib.Lazy;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.file.temp.TempFileEntry;
import ch.verno.server.file.temp.TempFileHandler;
import jakarta.annotation.Nonnull;

public class TempFileBo {

  @Nonnull private final Lazy<TempFileHandler> tempFileHandler;

  protected TempFileBo(@Nonnull final ServerBean serverBean) {
    this.tempFileHandler = Lazy.of(() -> serverBean.get(TempFileHandler.class));
  }

  @Nonnull
  public String store(@Nonnull final String filename,
                      @Nonnull final byte[] data) {
    return tempFileHandler.get().store(filename, data);
  }

  @Nonnull
  public FileDto load(@Nonnull final String token) {
    return tempFileHandler.get().load(token);
  }

  @Nonnull
  public TempFileEntry resolveEntry(@Nonnull final String token) {
    return tempFileHandler.get().resolveEntry(token);
  }

  public void delete(@Nonnull final String token) {
    tempFileHandler.get().delete(token);
  }

}
