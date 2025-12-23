package ch.verno.server.service;

import ch.verno.common.db.dto.GenderDto;
import ch.verno.common.db.service.IGenderService;
import ch.verno.server.mapper.GenderMapper;
import ch.verno.server.repository.GenderRepository;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GenderService implements IGenderService {

  @Nonnull
  private final GenderRepository genderRepository;

  public GenderService(@Nonnull final GenderRepository genderRepository) {
    this.genderRepository = genderRepository;
  }

  @Nonnull
  @Transactional(readOnly = true)
  @Override
  public GenderDto getGenderById(@Nonnull final Long id) {
    final var genderOptional = genderRepository.findById(id);
    if (genderOptional.isEmpty()) {
      throw new IllegalArgumentException("Gender not found with id: " + id);
    }

    return GenderMapper.toDto(genderOptional.get());
  }

  @Nonnull
  @Transactional(readOnly = true)
  @Override
  public List<GenderDto> getAllGenders() {
    return genderRepository.findAll().stream().map(GenderMapper::toDto).toList();
  }

}
