package ch.verno.common.participant;

import ch.verno.server.participant.entity.ParticipantEntity;
import ch.verno.ui.verno.participant.ParticipantDto;
import jakarta.annotation.Nonnull;

import java.time.LocalDate;
import java.time.Period;

public class ParticipantMapper {

  @Nonnull
  public static ParticipantDto toDto(ParticipantEntity entity) {
    return new ParticipantDto(
        entity.getId(),
        entity.getFirstname(),
        entity.getLastname(),
        calculateAge(entity.getBirthdate()),
        entity.getEmail(),
        entity.getPhone()
    );
  }

  private static int calculateAge(LocalDate birthdate) {
    LocalDate today = java.time.LocalDate.now();
    return Period.between(birthdate, today).getYears();
  }

}
