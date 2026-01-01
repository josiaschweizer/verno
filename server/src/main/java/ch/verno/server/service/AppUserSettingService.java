package ch.verno.server.service;

import ch.verno.common.db.dto.AppUserSettingDto;
import ch.verno.common.db.service.IAppUserSettingService;
import ch.verno.common.exceptions.NotFoundException;
import ch.verno.common.exceptions.NotFoundReason;
import ch.verno.db.entity.user.AppUserSettingEntity;
import ch.verno.server.mapper.AppUserSettingMapper;
import ch.verno.server.repository.AppUserRepository;
import ch.verno.server.repository.AppUserSettingRepository;
import jakarta.annotation.Nonnull;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AppUserSettingService implements IAppUserSettingService {

  @Nonnull
  private final AppUserSettingRepository repository;
  @Nonnull
  private final AppUserRepository appUserRepository;

  public AppUserSettingService(@Nonnull final AppUserSettingRepository repository,
                               @Nonnull final AppUserRepository appUserRepository) {
    this.repository = repository;
    this.appUserRepository = appUserRepository;
  }

  @Nonnull
  @Override
  @Transactional
  public AppUserSettingDto updateAppUserSetting(@Nonnull final AppUserSettingDto dto) {
    final var existingSetting = repository.findByUserId(dto.getUserId())
            .orElseThrow(() -> new NotFoundException(NotFoundReason.USER_SETTING_BY_USER_ID_NOT_FOUND));

    AppUserSettingMapper.updateEntity(existingSetting, dto);
    final var savedEntity = repository.save(existingSetting);
    return AppUserSettingMapper.toDto(savedEntity);
  }

  @Nonnull
  @Override
  @Transactional
  public AppUserSettingDto createAppUserSetting(@Nonnull final AppUserSettingDto dto) {
    final var user = appUserRepository.findById(dto.getUserId())
            .orElseThrow(() -> new NotFoundException(NotFoundReason.APP_USER_NOT_FOUND));

    try {
      final var entity = new AppUserSettingEntity(user, dto.getTheme(), dto.getLanguageTag());
      final var savedEntity = repository.save(entity);
      return AppUserSettingMapper.toDto(savedEntity);
    } catch (DataIntegrityViolationException ex) {
      final var existing = repository.findByUserId(dto.getUserId());
      if (existing.isPresent()) {
        return updateAppUserSetting(dto);
      }

      throw ex;
    }
  }

  @Nonnull
  @Override
  @Transactional(readOnly = true)
  public AppUserSettingDto getAppUserSettingById(@Nonnull final Long id) {
    final var foundById = repository.findById(id);
    if (foundById.isEmpty()) {
      throw new NotFoundException(NotFoundReason.USER_SETTING_BY_ID_NOT_FOUND);
    }
    return AppUserSettingMapper.toDto(foundById.get());
  }

  @Nonnull
  @Transactional(readOnly = true)
  public AppUserSettingDto getAppUserSettingByUserId(@Nonnull final Long id) {
    final var foundByUserId = repository.findByUserId(id);
    if (foundByUserId.isEmpty()) {
      throw new NotFoundException(NotFoundReason.USER_SETTING_BY_USER_ID_NOT_FOUND);
    }

    return AppUserSettingMapper.toDto(foundByUserId.get());
  }

  @Nonnull
  @Override
  @Transactional(readOnly = true)
  public List<AppUserSettingDto> getAppUserSettings() {
    return repository.findAll().stream()
            .map(AppUserSettingMapper::toDto)
            .toList();
  }

  @Nonnull
  @Override
  @Transactional
  public AppUserSettingDto saveAppUserSetting(@Nonnull final AppUserSettingDto dto) {
    if (dto.getId() == null) {
      return createAppUserSetting(dto);
    }
    return updateAppUserSetting(dto);
  }
}
