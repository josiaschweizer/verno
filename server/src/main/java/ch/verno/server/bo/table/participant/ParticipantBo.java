package ch.verno.server.bo.table.participant;

import ch.verno.contract.dto.result.base.SaveResult;
import ch.verno.contract.dto.result.error.SaveErrorCode;
import ch.verno.contract.dto.table.participant.ParticipantDto;
import ch.verno.lib.Lazy;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.service.intern.table.participant.ParticipantService;
import jakarta.annotation.Nonnull;
import org.jetbrains.annotations.NonNls;
import org.springframework.dao.DataIntegrityViolationException;

public class ParticipantBo {

  @NonNls public static final String DUPLICATE_KEY_VALUE_VIOLATES_UNIQUE_CONSTRAINT = "duplicate key value violates unique constraint";
  @NonNls public static final String EMAIL = "email";

  @Nonnull private final Lazy<ParticipantService> participantService;

  protected ParticipantBo(@Nonnull final ServerBean serverBean) {
    this.participantService = Lazy.of(() -> serverBean.get(ParticipantService.class));
  }

  @Nonnull
  public SaveResult<ParticipantDto> saveParticipant(@Nonnull final ParticipantDto dto) {
    try {
      final var participant = participantService.get().save(dto);
      participantService.get().flush();

      return SaveResult.success(participant);
    } catch (DataIntegrityViolationException exception) {
      if (isDuplicateParticipantEmail(exception)) {
        return SaveResult.failed(SaveErrorCode.PARTICIPANT_EMAIL_ALREADY_EXISTS);
      }

      return SaveResult.failed(SaveErrorCode.DATABASE_ERROR);
    }
  }

  private boolean isDuplicateParticipantEmail(@Nonnull final DataIntegrityViolationException exception) {
    final var cause = exception.getMostSpecificCause();
    final var message = cause.getMessage();

    return message != null
            && message.contains(DUPLICATE_KEY_VALUE_VIOLATES_UNIQUE_CONSTRAINT)
            && message.toLowerCase().contains(EMAIL);
  }

}
