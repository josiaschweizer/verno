package ch.verno.common.db.service;

import ch.verno.common.db.dto.table.GenderDto;
import jakarta.annotation.Nonnull;

import java.util.List;

public interface IGenderService {

  @Nonnull
  GenderDto getGenderById(@Nonnull Long id);

  @Nonnull
  List<GenderDto> getAllGenders();
}
