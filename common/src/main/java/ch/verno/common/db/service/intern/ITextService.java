package ch.verno.common.db.service.intern;

import ch.verno.common.db.dto.table.text.TextDto;
import ch.verno.lib.language.Language;
import jakarta.annotation.Nonnull;

import java.util.List;
import java.util.Map;

public interface ITextService {

  @Nonnull
  List<TextDto> findAll();

  @Nonnull
  TextDto getById(@Nonnull Long id);

  @Nonnull
  List<TextDto> findByIdentifier(@Nonnull String identifier);

  @Nonnull
  Map<Language, TextDto> findByIdentifierMap(@Nonnull String identifier);

  @Nonnull
  List<TextDto> findByIdentifierSubIdentifier(@Nonnull String identifier, @Nonnull String subIdentifier);

  @Nonnull
  Map<Language, TextDto> findByIdentifierSubIdentifierMap(@Nonnull String identifier,
                                                          @Nonnull String subIdentifier);

  @Nonnull
  TextDto findByIdentifierAndSubIdentifierAndLanguageCode(@Nonnull String identifier,
                                                          @Nonnull String subIdentifier,
                                                          @Nonnull Language language);

  void saveMultiple(@Nonnull List<TextDto> textDtos);

  void save(@Nonnull TextDto textDto);

  @Nonnull
  TextDto update(@Nonnull Long id,
                 @Nonnull TextDto textDto);

  @Nonnull
  TextDto create(@Nonnull TextDto textDto);
}
