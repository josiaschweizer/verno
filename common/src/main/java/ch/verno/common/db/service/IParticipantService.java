package ch.verno.common.db.service;

import ch.verno.common.db.dto.ParticipantDto;
import jakarta.annotation.Nonnull;

import java.util.List;

public interface IParticipantService {

  void createParticipant(@Nonnull ParticipantDto participantEntity);

  void updateParticipant(@Nonnull ParticipantDto participantEntity);

  @Nonnull
  ParticipantDto getParticipantById(@Nonnull Long id);

  @Nonnull
  List<ParticipantDto> getAllParticipants();
}
