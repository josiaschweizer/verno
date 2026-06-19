package ch.verno.server.service.intern.table.text;

import ch.verno.contract.dto.table.text.TextDto;
import ch.verno.db.entity.text.TextEntity;
import ch.verno.lib.Publ;
import ch.verno.lib.lib.language.Language;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.mapper.text.TextMapper;
import ch.verno.server.repository.text.TextRepository;
import ch.verno.server.service.base.AbstractEntityService;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
public class TextService extends AbstractEntityService<
        TextEntity,
        TextDto,
        TextRepository,
        TextMapper> {

  public TextService(@Nonnull final ServerBean bean) {
    super(bean.get(TextRepository.class), bean.get(TextMapper.class));
  }

  @Nonnull
  @Transactional(readOnly = true)
  public List<TextDto> findByIdentifier(@Nonnull final String identifier) {
    return getRepository()
            .findByIdentifier(identifier)
            .stream()
            .map(getMapper()::toSimpleDto)
            .toList();
  }

  @Nonnull
  @Transactional(readOnly = true)
  public Map<Language, TextDto> findByIdentifierMap(@Nonnull final String identifier) {
    return findByIdentifier(identifier)
            .stream()
            .collect(Collectors.toMap(
                    TextDto::getLanguage,
                    Function.identity()
            ));
  }

  @Nonnull
  @Transactional(readOnly = true)
  public List<TextDto> findByIdentifierSubIdentifier(@Nonnull final String identifier,
                                                     @Nonnull final String subIdentifier) {
    return getRepository()
            .findByIdentifierAndSubIdentifier(identifier, subIdentifier)
            .stream()
            .map(getMapper()::toSimpleDto)
            .toList();
  }

  @Nonnull
  @Transactional(readOnly = true)
  public Map<Language, TextDto> findByIdentifierSubIdentifierMap(@Nonnull final String identifier,
                                                                 @Nonnull final String subIdentifier) {
    return findByIdentifierSubIdentifier(identifier, subIdentifier)
            .stream()
            .collect(Collectors.toMap(
                    TextDto::getLanguage,
                    Function.identity()
            ));
  }

  @Nonnull
  @Transactional(readOnly = true)
  public Optional<TextDto> findByIdentifierAndSubIdentifierAndLanguageCode(@Nonnull final String identifier,
                                                                           @Nonnull final String subIdentifier,
                                                                           @Nonnull final Language language) {
    return getRepository()
            .findByIdentifierAndSubIdentifierAndLanguage(
                    identifier,
                    subIdentifier,
                    language
            )
            .map(getMapper()::toSimpleDto);
  }

  @Transactional
  public void saveMultiple(@Nonnull final List<TextDto> textDtos) {
    textDtos.forEach(this::save);
  }

  @Nonnull
  @Override
  public TextDto save(@Nonnull final TextDto dto) {
    final var existing = getRepository().findByIdentifierAndSubIdentifierAndLanguage(
            dto.getIdentifier(),
            Optional.ofNullable(dto.getSubIdentifier()).orElse(Publ.EMPTY_STRING),
            dto.getLanguage()
    );

    existing.ifPresent(textEntity -> dto.setId(textEntity.getId()));
    return super.save(dto);
  }

}