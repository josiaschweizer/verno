package ch.verno.contract.endpoint.participant;

import ch.verno.contract.dto.filter.ParticipantFilter;
import ch.verno.contract.dto.response.base.delete.DeleteResponse;
import ch.verno.contract.dto.response.base.save.SaveResponse;
import ch.verno.contract.dto.table.base.SortOrderDto;
import ch.verno.contract.dto.table.course.CourseDto;
import ch.verno.contract.dto.table.participant.ParticipantDto;
import ch.verno.contract.rpc.RpcEndpoint;
import jakarta.annotation.Nonnull;

import java.util.List;
import java.util.Optional;

@RpcEndpoint
public interface ParticipantResource {

  Optional<ParticipantDto> getParticipantById(@Nonnull Long id);

  @Nonnull
  List<ParticipantDto> getParticipants();

  @Nonnull
  List<ParticipantDto> getParticipants(@Nonnull ParticipantFilter filter,
                                       int offset,
                                       int limit,
                                       @Nonnull List<SortOrderDto> sortOrders);

  @Nonnull
  SaveResponse<ParticipantDto> saveParticipant(@Nonnull ParticipantDto dto);

  @Nonnull
  DeleteResponse deleteParticipantById(@Nonnull Long id);

  @Nonnull
  ParticipantDto enableParticipant(@Nonnull Long id);

  @Nonnull
  ParticipantDto disableParticipant(@Nonnull Long id);

  @Nonnull
  ParticipantDto addCourse(@Nonnull Long participantId,
                           @Nonnull CourseDto courseDto);

  @Nonnull
  ParticipantDto removeCourse(@Nonnull Long participantId,
                              @Nonnull CourseDto courseDto);

}
