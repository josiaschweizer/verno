package ch.verno.common.db.service.intern;

import ch.verno.common.db.dto.table.GenderDto;
import jakarta.annotation.Nonnull;

import java.util.List;
import java.util.Optional;

public interface IGenderService {

  @Nonnull
  GenderDto getGenderById(@Nonnull Long id);

  @Nonnull
  List<GenderDto> getAllGenders();

  Optional<GenderDto> getGenderByName(@Nonnull String name);

  void saveGender(@Nonnull GenderDto genderDto);

  @Nonnull
  GenderDto createGender(@Nonnull GenderDto genderDto);

  @Nonnull
  GenderDto updateGender(@Nonnull Long id,
                         @Nonnull GenderDto genderDto);
}
