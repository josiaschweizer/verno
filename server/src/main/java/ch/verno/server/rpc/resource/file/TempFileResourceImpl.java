package ch.verno.server.rpc.resource.file;

import ch.verno.contract.dto.file.temp.FileDto;
import ch.verno.contract.endpoint.file.TempFileResource;
import ch.verno.contract.rpc.RpcResource;
import ch.verno.lib.Lazy;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.bo.file.TempFileBo;
import jakarta.annotation.Nonnull;

@SuppressWarnings("unused")
@RpcResource(TempFileResource.class)
public class TempFileResourceImpl implements TempFileResource {

  @Nonnull private final Lazy<TempFileBo> tempFileBo;

  public TempFileResourceImpl(@Nonnull final ServerBean serverBean){
    this.tempFileBo = Lazy.of(() -> serverBean.get(TempFileBo.class));
  }

  @Nonnull
  @Override
  public FileDto loadFile(@Nonnull final String token) {
    return tempFileBo.get().load(token);
  }

  @Nonnull
  @Override
  public String store(@Nonnull final String filename, @Nonnull final byte[] fileBytes) {
    return tempFileBo.get().store(new FileDto(filename, fileBytes));
  }

  @Override
  public String store(@Nonnull final FileDto file) {
    return tempFileBo.get().store(file);
  }

  @Override
  public void delete(@Nonnull final String token) {
    tempFileBo.get().delete(token);
  }
}
