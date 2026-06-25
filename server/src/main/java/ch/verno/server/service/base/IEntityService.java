package ch.verno.server.service.base;

import jakarta.annotation.Nonnull;

import java.util.List;
import java.util.Optional;

public interface IEntityService<DTO> {

  @Nonnull
  Optional<DTO> findById(@Nonnull Long id);

  @Nonnull
  List<DTO> findAll();

  @Nonnull
  DTO save(@Nonnull DTO dto);

  boolean deleteById(@Nonnull Long id);

  void delete(@Nonnull DTO dto);

  long count();

  void flush();

}