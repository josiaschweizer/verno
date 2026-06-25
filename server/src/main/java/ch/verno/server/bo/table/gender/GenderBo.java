package ch.verno.server.bo.table.gender;

import ch.verno.common.db.constants.text.TextConstants;
import ch.verno.common.lib.gender.GenderUtil;
import ch.verno.contract.dto.table.gender.GenderDto;
import ch.verno.contract.dto.table.text.TextDto;
import ch.verno.db.entity.gender.GenderEntity;
import ch.verno.lib.Lazy;
import ch.verno.lib.New;
import ch.verno.lib.lib.language.Language;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.mapper.db.gender.GenderMapper;
import ch.verno.server.repository.gender.GenderRepository;
import ch.verno.server.service.intern.table.text.TextService;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Transactional
public class GenderBo {

  @Nonnull private final Lazy<TextService> textService;
  @Nonnull private final Lazy<GenderMapper> genderMapper;
  @Nonnull private final Lazy<GenderRepository> genderRepository;

  protected GenderBo(@Nonnull final ServerBean serverBean) {
    textService = Lazy.of(() -> serverBean.get(TextService.class));
    genderMapper = Lazy.of(() -> serverBean.get(GenderMapper.class));
    genderRepository = Lazy.of(() -> serverBean.get(GenderRepository.class));
  }

  /**
   * Finds a gender by its internal or translated name and enriches it with
   * <p>
   * its configured display texts.
   *
   * @param name internal or translated gender name
   * @return the matching gender, if one exists
   */
  @Nonnull
  @Transactional(readOnly = true)
  public Optional<GenderDto> findByName(@Nonnull final String name) {
    final var safeName = GenderUtil.translateToInternalGender(name);
    return genderRepository.get().findByName(safeName)
            .map(this::toDtoWithTranslations);
  }

  /**
   * Finds all genders and enriches them with their configured display texts.
   *
   * @return all configured genders
   */
  @Nonnull
  @Transactional(readOnly = true)
  public List<GenderDto> findAll() {
    return genderRepository.get().findAll()
            .stream()
            .map(this::toDtoWithTranslations)
            .toList();
  }

  /**
   * Saves a gender and its user-defined translations.
   *
   * @param dto gender to save
   * @return the saved gender including its translations
   */
  @Nonnull
  public GenderDto save(@Nonnull final GenderDto dto) {
    final var oldEntity = findEntityById(dto.getId());
    final var oldGenderName = oldEntity
            .map(GenderEntity::getName)
            .orElse(null);
    final var savedEntity = saveGender(dto, oldEntity.orElse(null));
    if (dto.getUserDisplayTexts() != null) {
      saveUserTranslations(savedEntity.getName(), oldGenderName, dto.getUserDisplayTexts());
    }

    return toDtoWithTranslations(savedEntity);
  }

  @Nonnull

  private GenderEntity saveGender(@Nonnull final GenderDto dto,
                                  @Nullable final GenderEntity existingEntity) {
    if (existingEntity == null) {
      return genderRepository.get().save(genderMapper.get().toNewEntity(dto));
    }

    genderMapper.get().updateEntity(existingEntity, dto);
    return genderRepository.get().save(existingEntity);
  }

  @Nonnull
  private Optional<GenderEntity> findEntityById(@Nullable final Long id) {
    if (id == null) {
      return Optional.empty();
    }
    return genderRepository.get().findById(id);
  }

  @Nonnull

  private GenderDto toDtoWithTranslations(@Nonnull final GenderEntity entity) {
    final var dto = genderMapper.get().toSimpleDto(entity);
    dto.setUserDisplayTexts(getUserTranslations(entity.getName()));
    return dto;
  }

  @Nonnull
  private Map<Language, TextDto> getUserTranslations(@Nonnull final String genderName) {
    return textService.get().findByIdentifierSubIdentifierMap(
            TextConstants.GENDER_IDENTIFIER,
            genderName
    );
  }

  private void saveUserTranslations(@Nonnull final String newGenderName,
                                    @Nullable final String oldGenderName,
                                    @Nonnull final Map<Language, TextDto> newTranslations) {
    final var oldTranslations = oldGenderName == null
            ? Map.<Language, TextDto>of()
            : getUserTranslations(oldGenderName);

    deleteRemovedTranslations(oldTranslations, newTranslations);

    final var textsToSave = New.<TextDto>list();
    newTranslations.forEach((language, textDto) -> {
      textDto.setIdentifier(TextConstants.GENDER_IDENTIFIER);
      textDto.setSubIdentifier(newGenderName);
      textDto.setLanguage(language);
      textsToSave.add(textDto);
    });

    if (!textsToSave.isEmpty()) {
      textService.get().saveMultiple(textsToSave);
    }

    deleteTranslationsWithOldGenderName(oldGenderName, newGenderName, oldTranslations);
  }

  private void deleteRemovedTranslations(@Nonnull final Map<Language, TextDto> oldTranslations,
                                         @Nonnull final Map<Language, TextDto> newTranslations) {
    oldTranslations.forEach((language, oldTextDto) -> {
      if (!newTranslations.containsKey(language)) {
        textService.get().delete(oldTextDto);
      }
    });
  }

  private void deleteTranslationsWithOldGenderName(@Nullable final String oldGenderName,
                                                   @Nonnull final String newGenderName,
                                                   @Nonnull final Map<Language, TextDto> oldTranslations) {
    if (oldGenderName == null || oldGenderName.equals(newGenderName)) {
      return;
    }

    oldTranslations.values().forEach(dto -> textService.get().delete(dto));
  }
}