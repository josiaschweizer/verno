package ch.verno.server.service.base;

import ch.verno.contract.dto.response.base.delete.DeleteResponse;
import jakarta.annotation.Nonnull;

import java.util.List;

public interface IEntityService<DTO> {

  @Nonnull
  List<DTO> findAll();

  @Nonnull
  DTO save(@Nonnull DTO dto);

  @Nonnull
  DeleteResponse delete(@Nonnull DTO dto);

  long count();

  void flush();

}