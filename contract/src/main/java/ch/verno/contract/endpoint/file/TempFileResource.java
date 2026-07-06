package ch.verno.contract.endpoint.file;

import ch.verno.contract.dto.file.temp.FileDto;
import ch.verno.contract.rpc.RpcEndpoint;
import jakarta.annotation.Nonnull;

@RpcEndpoint
public interface TempFileResource {

  @Nonnull
  FileDto loadFile(@Nonnull String token);

  @Nonnull
  String store(@Nonnull String filename, @Nonnull byte[] fileBytes);

  String store(@Nonnull FileDto file);

  void delete(@Nonnull String token);

  @Nonnull
  String issueAccessToken(@Nonnull String fileToken);
}
