package ch.verno.common.db.service.intern;

import ch.verno.common.db.dto.table.text.TextDto;
import jakarta.annotation.Nonnull;

import java.util.List;

public interface ITextService {

  @Nonnull
  List<TextDto> findAll();

  @Nonnull
  TextDto getById(@Nonnull Long id);

  void save(@Nonnull TextDto textDto);

  @Nonnull
  TextDto update(@Nonnull Long id,
                 @Nonnull TextDto textDto);
}
