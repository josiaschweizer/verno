package ch.verno.server.service;

import ch.verno.common.db.dto.ParticipantDto;
import ch.verno.common.db.service.IParticipantService;
import ch.verno.server.mapper.ParticipantMapper;
import ch.verno.server.repository.ParticipantRepository;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ParticipantService implements IParticipantService {

  @Nonnull
  private final ParticipantRepository participantRepository;

  public ParticipantService(@Nonnull final ParticipantRepository participantRepository) {
    this.participantRepository = participantRepository;
  }

  @Override
  @Transactional
  public void createParticipant(@Nonnull final ParticipantDto participantEntity) {
    participantRepository.save(ParticipantMapper.toEntity(participantEntity));
  }

  @Override
  @Transactional
  public void updateParticipant(@Nonnull final ParticipantDto participantEntity) {
    participantRepository.update(ParticipantMapper.toEntity(participantEntity));
  }

  @Nonnull
  @Override
  @Transactional(readOnly = true)
  public ParticipantDto getParticipantById(@Nonnull final Long id) {
    final var foundById = participantRepository.findById(id);
    if (foundById.isEmpty()) {
      throw new IllegalArgumentException("Participant not found with id: " + id);
    }

    return ParticipantMapper.toDto(foundById.get());
  }

  @Nonnull
  @Override
  @Transactional(readOnly = true)
  public List<ParticipantDto> getAllParticipants() {
    return participantRepository.findAll().stream()
        .map(ParticipantMapper::toDto)
        .toList();
  }
}
