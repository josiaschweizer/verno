package ch.verno.common.participant;

import ch.verno.common.base.components.entry.phonenumber.PhoneNumber;
import ch.verno.server.participant.entity.ParticipantEntity;
import ch.verno.ui.verno.participant.dto.GenderDto;
import ch.verno.ui.verno.participant.dto.ParticipantDto;
import jakarta.annotation.Nonnull;

public class ParticipantMapper {

  @Nonnull
  public static ParticipantDto toDto(@Nonnull final ParticipantEntity entity,
                                     @Nonnull final GenderDto genderDto) {
    return new ParticipantDto(
        entity.getId(),
        entity.getFirstname(),
        entity.getLastname(),
        entity.getBirthdate(),
        genderDto,
        entity.getEmail(),
        PhoneNumber.fromString(entity.getPhone())
    );
  }

}
