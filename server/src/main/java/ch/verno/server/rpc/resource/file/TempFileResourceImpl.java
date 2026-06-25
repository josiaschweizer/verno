package ch.verno.server.rpc.resource.file;

import ch.verno.contract.dto.file.temp.FileDto;
import ch.verno.contract.endpoint.file.TempFileResource;
import ch.verno.contract.rpc.RpcResource;
import ch.verno.server.bean.ServerBean;
import jakarta.annotation.Nonnull;

@RpcResource(TempFileResource.class)
public class TempFileResourceImpl implements TempFileResource {

  public TempFileResourceImpl(@Nonnull final ServerBean serverBean){

  }

  @Nonnull
  @Override
  public FileDto loadFile(@Nonnull final String token) {
    return null;
  }

  @Nonnull
  @Override
  public String store(@Nonnull final String filename, @Nonnull final byte[] fileBytes) {
    return "";
  }

  @Override
  public String store(@Nonnull final FileDto file) {
    return "";
  }

  @Override
  public void delete(@Nonnull final String token) {

  }
}
