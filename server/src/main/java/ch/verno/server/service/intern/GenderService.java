package ch.verno.server.service.intern;

import ch.verno.common.db.constants.text.TextConstants;
import ch.verno.common.db.dto.table.GenderDto;
import ch.verno.common.db.dto.table.text.TextDto;
import ch.verno.common.db.service.intern.IGenderService;
import ch.verno.common.db.service.intern.ITextService;
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
    saveGenderEntity(genderDto);

    if (genderDto.getUserDisplayTexts() != null) {
      saveUserTranslations(genderDto.getUserDisplayTexts());
    }
  }

  private void saveGenderEntity(@Nonnull final GenderDto genderDto) {
    final GenderEntity entity;
    if (genderDto.getId() == null || genderDto.getId() == 0) {
      entity = GenderEntity.empty();
      entity.setTenant(TenantEntity.ref(TenantContext.getRequired()));
    } else {
      entity = genderRepository.findById(genderDto.getId()).orElseThrow(() -> new DBNotFoundException(DBNotFoundReason.GENDER_BY_ID_NOT_FOUND, genderDto.getId()));
    }

    entity.setName(genderDto.getName());
    entity.setDescription(genderDto.getDescription());
    genderRepository.save(entity);
  }

  @Nonnull
  private Map<Language, TextDto> getUserTranslation(@Nonnull final String entityName) {
    final var identifier = TextConstants.GENDER_IDENTIFIER;
    return textService.findByIdentifierSubIdentifierMap(identifier, entityName);
  }

  private void saveUserTranslations(@Nonnull final Map<Language, TextDto> displayTexts) {
    final var texts = New.<TextDto>arrayList();
    displayTexts.forEach(((language, textDto) -> texts.add(textDto)));

    if (!texts.isEmpty()) {
      textService.saveMultiple(texts);
    }
  }

}
