package ch.verno.server.service.base;

import jakarta.annotation.Nonnull;

import java.util.List;

public interface IEntityService<DTO> {

  @Nonnull
  List<DTO> findAll();

  @Nonnull
  DTO save(@Nonnull DTO dto);

  void delete(@Nonnull DTO dto);

  long count();

  void flush();

}