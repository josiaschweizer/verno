package ch.verno.server.service;

import ch.verno.common.db.dto.table.MandantSettingDto;
import ch.verno.common.db.service.IMandantSettingService;
import ch.verno.common.exceptions.db.DBNotFoundException;
import ch.verno.common.exceptions.db.DBNotFoundReason;
import ch.verno.server.mapper.MandantSettingMapper;
import ch.verno.server.repository.MandantSettingRepository;
import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MandantSettingService implements IMandantSettingService {

  //TODO DIFFERENT MANDANTS MANAGEMENT - currently only one mandant is supported

  @Nonnull
  private final MandantSettingRepository mandantSettingRepository;

  @Autowired
  public MandantSettingService(@Nonnull final MandantSettingRepository mandantSettingRepository) {
    this.mandantSettingRepository = mandantSettingRepository;
  }

  @Nonnull
  @Override
  @Transactional
  public MandantSettingDto createMandanSetting(@Nonnull final MandantSettingDto mandantSettingDto) {
    final var entity = MandantSettingMapper.toEntity(mandantSettingDto);
    final var saved = mandantSettingRepository.save(entity);
    return MandantSettingMapper.toDto(saved);
  }

  @Nonnull
  @Override
  @Transactional
  public MandantSettingDto updateMandantSetting(@Nonnull final MandantSettingDto mandantSettingDto) {
    final var id = mandantSettingDto.getId();
    if (id == null) {
      throw new IllegalArgumentException("MandantSettingDto.id must not be null for update");
    }

    final var existing = mandantSettingRepository.findById(id);
    if (existing.isEmpty()) {
      throw new DBNotFoundException(DBNotFoundReason.MANDANT_SETTINGS_BY_ID_NOT_FOUND, id);
    }

    final var entity = existing.get();
    MandantSettingMapper.updateEntity(mandantSettingDto, entity);

    final var saved = mandantSettingRepository.save(entity);
    return MandantSettingMapper.toDto(saved);
  }

  @Nonnull
  @Override
  @Transactional(readOnly = true)
  public MandantSettingDto getMandantSettingById(@Nonnull final Long id) {
    final var foundById = mandantSettingRepository.findById(id);
    if (foundById.isEmpty()) {
      throw new DBNotFoundException(DBNotFoundReason.MANDANT_SETTINGS_BY_ID_NOT_FOUND, id);
    }

    return MandantSettingMapper.toDto(foundById.get());
  }

  @Nonnull
  @Override
  @Transactional(readOnly = true)
  public List<MandantSettingDto> getAllMandantSettings() {
    return mandantSettingRepository.findAll().stream()
            .map(MandantSettingMapper::toDto)
            .toList();
  }

  @Nonnull
  @Override
  @Transactional(readOnly = true)
  public MandantSettingDto getSingleMandantSetting() {
    return mandantSettingRepository.findAll().stream()
            .findFirst()
            .map(MandantSettingMapper::toDto)
            .orElse(saveSingleMandantSetting(new MandantSettingDto(1L, 8, 12, false, false, true, "Course Report")));
  }

  @Nonnull
  @Override
  @Transactional
  public MandantSettingDto getOrCreateSingleMandantSetting(@Nonnull final MandantSettingDto defaultDto) {
    final var existing = mandantSettingRepository.findAll().stream().findFirst();
    return existing.map(MandantSettingMapper::toDto).orElseGet(() -> createMandanSetting(defaultDto));
  }

  @Nonnull
  @Override
  @Transactional
  public MandantSettingDto saveSingleMandantSetting(@Nonnull final MandantSettingDto dto) {
    if (dto.getId() == null) {
      return createMandanSetting(dto);
    }
    return updateMandantSetting(dto);
  }
}