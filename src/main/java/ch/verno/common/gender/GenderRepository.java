package ch.verno.common.gender;

import ch.verno.server.gender.entity.GenderEntity;
import jakarta.annotation.Nonnull;

import java.util.List;
import java.util.Optional;

public interface GenderRepository {

  @Nonnull
  Optional<GenderEntity> findById(@Nonnull final Long id);

  @Nonnull
  List<GenderEntity> findAll();

  void save(@Nonnull final GenderEntity entity);

}
