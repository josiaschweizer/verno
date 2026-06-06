package ch.verno.server.service.intern.text;

import ch.verno.common.db.dto.table.text.TextDto;
import ch.verno.common.db.service.intern.ITextService;
import ch.verno.common.exceptions.db.DBNotFoundException;
import ch.verno.common.exceptions.db.DBNotFoundReason;
import ch.verno.db.entity.text.TextEntity;
import ch.verno.lib.language.Language;
import ch.verno.server.mapper.text.TextMapper;
import ch.verno.server.repository.TextRepository;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class TextService implements ITextService {

  @Nonnull private final TextRepository textRepository;

  public TextService(@Nonnull TextRepository textRepository) {
    this.textRepository = textRepository;
  }

  @Nonnull
  @Override
  @Transactional(readOnly = true)
  public List<TextDto> findAll() {
    return textRepository.findAll()
            .stream()
            .map(TextMapper::toDto)
            .toList();
  }

  @Nonnull
  @Override
  @Transactional(readOnly = true)
  public TextDto getById(@Nonnull Long id) {
    return TextMapper.toDto(getEntityById(id));
  }

  @Nonnull
  private TextEntity getEntityById(@Nonnull Long id) {
    final var byId = textRepository.findById(id);
    if (byId.isEmpty()) {
      throw new DBNotFoundException(DBNotFoundReason.TEXT_BY_ID_NOT_FOUND, id);
    }

    return byId.get();
  }

  @Nonnull
  @Override
  @Transactional(readOnly = true)
  public List<TextDto> findByIdentifier(@Nonnull String identifier) {
    return textRepository.findByIdentifier(identifier)
            .stream()
            .map(TextMapper::toDto)
            .toList();
  }

  @Nonnull
  @Override
  @Transactional(readOnly = true)
  public Map<Language, TextDto> findByIdentifierMap(@Nonnull String identifier) {
    return textRepository.findByIdentifier(identifier)
            .stream()
            .map(TextMapper::toDto)
            .collect(Collectors.toMap(
                    TextDto::getLanguage,
                    Function.identity()
            ));
  }

  @Nonnull
  @Override
  @Transactional(readOnly = true)
  public List<TextDto> findByIdentifierSubIdentifier(@Nonnull String identifier, @Nonnull String subIdentifier) {
    return textRepository.findByIdentifierAndSubIdentifier(identifier, subIdentifier)
            .stream()
            .map(TextMapper::toDto)
            .toList();
  }

  @Nonnull
  @Override
  @Transactional(readOnly = true)
  public Map<Language, TextDto> findByIdentifierSubIdentifierMap(@Nonnull String identifier,
                                                                 @Nonnull String subIdentifier) {
    return textRepository.findByIdentifierAndSubIdentifier(identifier, subIdentifier)
            .stream()
            .map(TextMapper::toDto)
            .collect(Collectors.toMap(
                    TextDto::getLanguage,
                    Function.identity()
            ));
  }

  @Nonnull
  @Override
  @Transactional(readOnly = true)
  public TextDto findByIdentifierAndSubIdentifierAndLanguageCode(@Nonnull String identifier,
                                                                 @Nonnull String subIdentifier,
                                                                 @Nonnull Language language) {
    final var foundOptional = textRepository.findByIdentifierAndSubIdentifierAndLanguage(identifier, subIdentifier, language);
    return foundOptional.map(TextMapper::toDto).orElseGet(TextDto::empty);
  }

  @Override
  @Transactional
  public void saveMultiple(@Nonnull final List<TextDto> textDtos) {
    for (final var textDto : textDtos) {
      save(textDto);
    }
  }

  @Override
  @Transactional
  public void save(@Nonnull TextDto textDto) {
    final var existing = textRepository.findByIdentifierAndSubIdentifierAndLanguage(
            textDto.getIdentifier(),
            textDto.getSubIdentifier(),
            textDto.getLanguage()
    );

    if (existing.isEmpty()) {
      create(textDto);
    } else {
      update(existing.get().getId(), textDto);
    }
  }

  @Nonnull
  @Override
  @Transactional
  public TextDto update(@Nonnull final Long id,
                        @Nonnull final TextDto textDto) {
    final var existing = getEntityById(id);

    existing.setIdentifier(textDto.getIdentifier());
    existing.setSubIdentifier(textDto.getSubIdentifier());
    existing.setText(textDto.getText());
    existing.setLanguageCode(textDto.getLanguage().getCode());

    return TextMapper.toDto(textRepository.save(existing));
  }

  @Nonnull
  @Override
  @Transactional
  public TextDto create(@Nonnull final TextDto textDto) {
    final var saveResult = textRepository.save(TextMapper.toEntity(textDto));
    return TextMapper.toDto(saveResult);
  }
}
