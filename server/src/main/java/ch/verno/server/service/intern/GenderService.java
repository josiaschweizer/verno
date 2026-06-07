package ch.verno.server.service.intern;

import ch.verno.common.db.constants.text.TextConstants;
import ch.verno.common.db.dto.table.GenderDto;
import ch.verno.common.db.dto.table.text.TextDto;
import ch.verno.common.server.service.intern.IGenderService;
import ch.verno.common.server.service.intern.ITextService;
import ch.verno.common.exceptions.db.DBNotFoundException;
import ch.verno.common.exceptions.db.DBNotFoundReason;
import ch.verno.common.gate.GlobalInterface;
import ch.verno.common.lib.gender.GenderUtil;
import ch.verno.common.tenant.TenantContext;
import ch.verno.db.entity.GenderEntity;
import ch.verno.db.entity.tenant.TenantEntity;
import ch.verno.lib.New;
import ch.verno.lib.language.Language;
import ch.verno.server.mapper.GenderMapper;
import ch.verno.server.repository.GenderRepository;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class GenderService implements IGenderService {

  @Nonnull private final GenderRepository genderRepository;
  @Nonnull private final ITextService textService;

  public GenderService(@Nonnull GlobalInterface globalInterface) {
    this.genderRepository = globalInterface.getService(GenderRepository.class);
    this.textService = globalInterface.getService(ITextService.class);
  }

  @Nonnull
  @Override
  @Transactional(readOnly = true)
  public GenderDto getGenderById(@Nonnull final Long id) {
    final var genderOptional = genderRepository.findById(id);
    if (genderOptional.isEmpty()) {
      throw new DBNotFoundException(DBNotFoundReason.GENDER_BY_ID_NOT_FOUND, id);
    }

    return GenderMapper.toDto(genderOptional.get());
  }

  @Nonnull
  @Override
  @Transactional(readOnly = true)
  public List<GenderDto> getAllGenders() {
    return genderRepository.findAll()
            .stream()
            .map(entity -> {
              final var userTranslation = getUserTranslation(entity.getName());
              return GenderMapper.toDto(entity, userTranslation);
            }).toList();
  }

  @Nonnull
  @Override
  @Transactional(readOnly = true)
  public Optional<GenderDto> getGenderByName(@Nonnull final String name) {
    final var internalName = GenderUtil.translateToInternalGender(name);
    final var gender = genderRepository.findByName(internalName);
    return gender.map(GenderMapper::toDto);
  }

  @Override
  @Transactional
  public void saveGender(@Nonnull GenderDto genderDto) {
    if (genderDto.getId() == null) {
      createGender(genderDto);
    } else {
      updateGender(genderDto.getId(), genderDto);
    }

    if (genderDto.getUserDisplayTexts() != null) {
      saveUserTranslations(genderDto);
    }
  }


  @Nonnull
  @Override
  @Transactional
  public GenderDto createGender(@Nonnull final GenderDto genderDto) {
    final var entity = GenderMapper.toNewEntity(
            genderDto,
            TenantEntity.ref(TenantContext.getRequired())
    );

    return GenderMapper.toDto(genderRepository.save(entity));
  }

  @Nonnull
  @Override
  @Transactional
  public GenderDto updateGender(@Nonnull final Long id,
                                @Nonnull final GenderDto genderDto) {
    final var existing = getGenderEntityById(id);
    existing.setName(genderDto.getName());
    existing.setDescription(genderDto.getDescription());

    final var saved = genderRepository.save(existing);
    return GenderMapper.toDto(saved);
  }

  @Nonnull
  private Map<Language, TextDto> getUserTranslation(@Nonnull final String entityName) {
    final var identifier = TextConstants.GENDER_IDENTIFIER;
    return textService.findByIdentifierSubIdentifierMap(identifier, entityName);
  }

  private void saveUserTranslations(@Nonnull final GenderDto genderDto) {
    if (genderDto.getId() != null) {
      deletePotentialTranslations(genderDto);
    }
    if (genderDto.getUserDisplayTexts() == null || genderDto.getUserDisplayTexts().isEmpty()) {
      return;
    }

    Map<Language, TextDto> displayTexts = genderDto.getUserDisplayTexts();
    final var texts = New.<TextDto>arrayList();
    displayTexts.forEach(((language, textDto) -> texts.add(textDto)));

    if (!texts.isEmpty()) {
      textService.saveMultiple(texts);
    }
  }

  private void deletePotentialTranslations(@Nonnull final GenderDto genderDto) {
    final var oldTranslations = getUserTranslation(genderDto.getName());
    if (!oldTranslations.isEmpty()) {
      final var newTranslations = genderDto.getUserDisplayTexts();
      oldTranslations.forEach((language, text) -> {
        if (!newTranslations.containsKey(language)) {
          textService.delete(text);
        }
      });
    }
  }

  @Nonnull
  private GenderEntity getGenderEntityById(@Nonnull final Long id) {
    return genderRepository.findById(id)
            .orElseThrow(() -> new DBNotFoundException(DBNotFoundReason.GENDER_BY_ID_NOT_FOUND, id));
  }

}
