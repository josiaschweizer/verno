package ch.verno.common.participant;

import ch.verno.server.participant.entity.ParticipantEntity;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ParticipantService {

  @Nonnull
  private final ParticipantRepository participantRepository;

  public ParticipantService(@Nonnull final ParticipantRepository participantRepository) {
    this.participantRepository = participantRepository;
  }

  @Transactional
  public void createParticipant(@Nonnull final ParticipantEntity participantEntity) {
    participantRepository.save(participantEntity);
  }

  @Nonnull
  @Transactional(readOnly = true)
  public ParticipantEntity getParticipantById(@Nonnull final Long id) {
    return participantRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Participant not found with id: " + id));
  }

  @Nonnull
  @Transactional(readOnly = true)
  public List<ParticipantEntity> getAllParticipants() {
    return participantRepository.findAll();
  }
}
