package ch.verno.server.bo.table.participant;

import ch.verno.contract.dto.response.base.save.SaveErrorCode;
import ch.verno.contract.dto.response.base.save.SaveResponse;
import ch.verno.contract.dto.table.course.CourseDto;
import ch.verno.contract.dto.table.participant.ParticipantDto;
import ch.verno.lib.Lazy;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.bo.BoFactory;
import ch.verno.server.bo.table.address.AddressBo;
import ch.verno.server.service.entity.participant.ParticipantService;
import jakarta.annotation.Nonnull;
import org.jetbrains.annotations.NonNls;
import org.springframework.dao.DataIntegrityViolationException;

public class ParticipantBo {

  @NonNls public static final String DUPLICATE_KEY_VALUE_VIOLATES_UNIQUE_CONSTRAINT = "duplicate key value violates unique constraint";
  @NonNls public static final String EMAIL = "email";

  @Nonnull private final Lazy<ParentBo> parentBo;
  @Nonnull private final Lazy<AddressBo> addressBo;
  @Nonnull private final Lazy<ParticipantService> participantService;

  protected ParticipantBo(@Nonnull final ServerBean serverBean) {
    this.parentBo = Lazy.of(() -> BoFactory.getInstance(serverBean).get(ParentBo.class));
    this.addressBo = Lazy.of(() -> BoFactory.getInstance(serverBean).get(AddressBo.class));
    this.participantService = Lazy.of(() -> serverBean.get(ParticipantService.class));
  }

  @Nonnull
  public ParticipantDto saveParticipant(@Nonnull final ParticipantDto participant) {
    final var relationEntitiesSaved = saveRelationEntities(participant);

    return participantService.get().save(relationEntitiesSaved);
  }

  @Nonnull
  private ParticipantDto saveRelationEntities(@Nonnull final ParticipantDto participant) {
    participant.setAddress(addressBo.get().findOrCreate(participant.getAddress()));
    participant.setParentOne(parentBo.get().saveOrEmpty(participant.getParentOne()));
    participant.setParentTwo(parentBo.get().saveOrEmpty(participant.getParentTwo()));

    return participant;
  }

  @Nonnull
  public ParticipantDto enableParticipant(@Nonnull final Long id) {
    final var participantOptional = participantService.get().findById(id);
    if (participantOptional.isEmpty()) {
      return ParticipantDto.empty();
    }

    final var participant = participantOptional.get();
    participant.setActive(true);
    return saveParticipant(participant);
  }

  @Nonnull
  public ParticipantDto disableParticipant(@Nonnull final Long id) {
    final var participantOptional = participantService.get().findById(id);
    if (participantOptional.isEmpty()) {
      return ParticipantDto.empty();
    }

    final var participant = participantOptional.get();
    participant.setActive(false);
    return saveParticipant(participant);
  }

  @Nonnull
  public ParticipantDto addCourse(@Nonnull final Long participantId,
                                  @Nonnull final CourseDto courseDto) {
    final var participant = participantService.get().findByIdRequired(participantId);
    participant.addCourse(courseDto);
    return participantService.get().save(participant);
  }

  @Nonnull
  public ParticipantDto removeCourse(@Nonnull final Long participantId,
                                     @Nonnull final CourseDto courseDto) {
    final var participant = participantService.get().findByIdRequired(participantId);
    participant.removeCourse(courseDto);
    return participantService.get().save(participant);
  }


  @Nonnull
  public SaveResponse<ParticipantDto> apiSaveParticipant(@Nonnull final ParticipantDto dto) {
    try {
      final var participant = participantService.get().save(dto);
      participantService.get().flush();

      return SaveResponse.success(participant);
    } catch (DataIntegrityViolationException exception) {
      if (isDuplicateParticipantEmail(exception)) {
        return SaveResponse.failed(SaveErrorCode.EMAIL_ALREADY_EXISTS);
      }

      return SaveResponse.failed(SaveErrorCode.DATABASE_ERROR);
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
