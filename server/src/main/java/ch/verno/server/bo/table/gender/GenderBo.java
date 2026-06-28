package ch.verno.server.bo.table.gender;

import ch.verno.common.db.constants.text.TextConstants;
import ch.verno.common.lib.gender.GenderUtil;
import ch.verno.contract.dto.table.gender.GenderDto;
import ch.verno.contract.dto.table.text.TextDto;
import ch.verno.lib.Lazy;
import ch.verno.lib.New;
import ch.verno.lib.lib.language.Language;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.service.intern.table.gender.GenderService;
import ch.verno.server.service.intern.table.text.TextService;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class GenderBo {

  @Nonnull private final Lazy<TextService> textService;
  @Nonnull private final Lazy<GenderService> genderService;

  protected GenderBo(@Nonnull final ServerBean serverBean) {
    this.textService = Lazy.of(() -> serverBean.get(TextService.class));
    this.genderService = Lazy.of(() -> serverBean.get(GenderService.class));
  }

  @Nonnull
  public Optional<GenderDto> findByName(@Nonnull final String name) {
    final var safeName = GenderUtil.translateToInternalGender(name);
    return genderService.get().findByName(safeName)
            .map(this::toDtoWithTranslations);
  }

  @Nonnull
  @Transactional(readOnly = true)
  public List<GenderDto> findAll() {
    return genderService.get().findAll()
            .stream()
            .map(this::toDtoWithTranslations)
            .toList();
  }

  @Nonnull
  public GenderDto save(@Nonnull final GenderDto dto) {
    final var oldGenderName = findOldGenderName(dto.getId());
    final var savedDto = genderService.get().save(dto);

    if (dto.getUserDisplayTexts() != null) {
      saveUserTranslations(savedDto.getName(), oldGenderName, dto.getUserDisplayTexts());
    }

    return toDtoWithTranslations(savedDto);
  }

  @Nullable
  private String findOldGenderName(@Nullable final Long id) {
    if (id == null) {
      return null;
    }

    return genderService.get().findById(id)
            .map(GenderDto::getName)
            .orElse(null);
  }

  @Nonnull
  private GenderDto toDtoWithTranslations(@Nonnull final GenderDto dto) {
    dto.setUserDisplayTexts(getUserTranslations(dto.getName()));
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