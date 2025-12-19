package ch.verno.server.persistence.mapper;

import ch.verno.domain.model.user.AppUser;
import ch.verno.server.persistence.entity.AppUserEntity;
import jakarta.annotation.Nonnull;

public class AppUserMapper {

  private AppUserMapper() {
  }

  @Nonnull
  public static AppUser toDomain(@Nonnull final AppUserEntity entity) {
    return new AppUser(
        entity.getId(),
        entity.getEmail(),
        entity.getCreatedAt()
    );
  }

  @Nonnull
  public static AppUserEntity toEntity(@Nonnull final AppUser domain) {
    return AppUserEntity.of(
        domain.getId(),
        domain.getEmail(),
        domain.getCreatedAt()
    );
  }
}