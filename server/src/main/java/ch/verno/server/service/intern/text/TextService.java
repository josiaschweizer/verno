package ch.verno.server.service.intern.text;

import ch.verno.common.db.dto.table.text.TextDto;
import ch.verno.common.db.service.intern.ITextService;
import ch.verno.common.exceptions.db.DBNotFoundException;
import ch.verno.common.exceptions.db.DBNotFoundReason;
import ch.verno.db.entity.text.TextEntity;
import ch.verno.server.mapper.text.TextMapper;
import ch.verno.server.repository.TextRepository;
import jakarta.annotation.Nonnull;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
  public TextDto findByIdentifier(@Nonnull String identifier) {
    final var byIdentifier = textRepository.findByIdentifier(identifier);
    return byIdentifier.map(TextMapper::toDto).orElseGet(TextDto::new);
  }

  @Override
  @Transactional
  public void save(@Nonnull TextDto textDto) {
    textRepository.save(TextMapper.toEntity(textDto));
  }

  @Nonnull
  @Override
  @Transactional
  public TextDto update(@Nonnull Long id,
                        @Nonnull TextDto textDto) {
    final var existing = getEntityById(id);

    existing.setIdentifier(textDto.getIdentifier());
    existing.setText(textDto.getText());
    existing.setLanguageCode(textDto.getLanguage().getCode());

    return TextMapper.toDto(textRepository.save(existing));
  }
}
