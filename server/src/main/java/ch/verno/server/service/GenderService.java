package ch.verno.server.service;

import ch.verno.common.db.service.IGenderService;
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
  public GenderEntity getGenderById(@Nonnull final Long id) {
    return genderRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Participant not found with id: " + id));
  }

  @Nonnull
  @Transactional(readOnly = true)
  @Override
  public List<GenderEntity> getAllGenders() {
    return genderRepository.findAll();
  }

}
