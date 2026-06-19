package ch.verno.server.service.intern.gender;

import ch.verno.common.db.constants.text.TextConstants;
import ch.verno.contract.dto.table.gender.GenderDto;
import ch.verno.contract.dto.table.text.TextDto;
import ch.verno.db.entity.gender.GenderEntity;
import ch.verno.lib.Lazy;
import ch.verno.lib.New;
import ch.verno.lib.lib.language.Language;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.mapper.base.MapperContext;
import ch.verno.server.mapper.gender.GenderMapper;
import ch.verno.server.mapper.gender.GenderTranslationContext;
import ch.verno.server.repository.gender.GenderRepository;
import ch.verno.server.service.base.AbstractEntityService;
import ch.verno.server.service.intern.text.TextService;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class GenderService extends AbstractEntityService<
        GenderEntity,
        GenderDto,
        GenderRepository,
        GenderMapper> {

  @Nonnull private final Lazy<TextService> textService;

  public GenderService(@Nonnull final ServerBean serverBean) {
    super(serverBean.get(GenderRepository.class), serverBean.get(GenderMapper.class));
    this.textService = Lazy.of(() -> serverBean.get(TextService.class));
  }

  @Nonnull
  @Transactional(readOnly = true)
  public Optional<GenderDto> findByName(@Nonnull final String name) {
    return getRepository()
            .findByName(name)
            .map(getMapper()::toSimpleDto);
  }

  @Nonnull
  @Override
  @Transactional(readOnly = true)
  public List<GenderDto> findAll() {
    return getRepository()
            .findAll()
            .stream()
            .map(entity -> {
              final var translations = new GenderTranslationContext(getUserTranslation(entity.getName()));
              final var context = MapperContext.empty().put(GenderTranslationContext.class, translations);

              return getMapper().toDto(entity, context);
            })
            .toList();
  }

  @Nonnull
  @Override
  public GenderDto save(@Nonnull final GenderDto dto) {
    final var saved = super.save(dto);

    if (saved.getUserDisplayTexts() != null) {
      saveUserTranslations(saved);
    }

    return saved;
  }

  @Nonnull
  private Map<Language, TextDto> getUserTranslation(@Nonnull final String entityName) {
    return textService.get().findByIdentifierSubIdentifierMap(
            TextConstants.GENDER_IDENTIFIER,
            entityName
    );
  }

  private void saveUserTranslations(@Nonnull final GenderDto dto) {
    if (dto.getId() != null) {
      deletePotentialTranslations(dto);
    }

    if (dto.getUserDisplayTexts() == null || dto.getUserDisplayTexts().isEmpty()) {
      return;
    }

    final var texts = New.<TextDto>arrayList();

    dto.getUserDisplayTexts().forEach((language, textDto) -> texts.add(textDto));

    if (!texts.isEmpty()) {
      textService.get().saveMultiple(texts);
    }
  }

  private void deletePotentialTranslations(@Nonnull final GenderDto dto) {
    final var oldTranslations = getUserTranslation(dto.getName());
    final var newTranslations = dto.getUserDisplayTexts();
    if (newTranslations == null) {
      return;
    }

    oldTranslations.forEach((language, text) -> {
      if (!newTranslations.containsKey(language)) {
        textService.get().delete(text);
      }
    });
  }
}