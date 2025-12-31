package ch.verno.server.service;

import ch.verno.common.db.dto.AppUserSettingDto;
import ch.verno.common.db.service.IAppUserSettingService;
import ch.verno.common.exceptions.NotFoundException;
import ch.verno.common.exceptions.NotFoundReason;
import ch.verno.server.mapper.AppUserSettingMapper;
import ch.verno.server.repository.AppUserSettingRepository;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AppUserSettingService implements IAppUserSettingService {

  @Nonnull
  private final AppUserSettingRepository repository;

  public AppUserSettingService(@Nonnull final AppUserSettingRepository repository) {
    this.repository = repository;
  }

  @Nonnull
  @Override
  @Transactional
  public AppUserSettingDto updateAppUserSetting(@Nonnull final AppUserSettingDto appUserSettingDto) {
    throw new RuntimeException("not implemented");
  }

  @Nonnull
  @Override
  @Transactional
  public AppUserSettingDto createAppUserSetting(@Nonnull final AppUserSettingDto appUserSettingDto) {
    throw new RuntimeException("not implemented");
  }

  @Nonnull
  @Override
  @Transactional(readOnly = true)
  public AppUserSettingDto getAppUserSettingById(@Nonnull final Long id) {
    throw new RuntimeException("not implemented");
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
