package ch.verno.rpc.client.participant;

import ch.verno.contract.dto.filter.ParticipantFilter;
import ch.verno.contract.dto.response.base.delete.DeleteResponse;
import ch.verno.contract.dto.response.base.save.SaveResponse;
import ch.verno.contract.dto.table.course.CourseDto;
import ch.verno.contract.dto.table.participant.ParticipantDto;
import ch.verno.contract.endpoint.participant.ParticipantResource;
import ch.verno.lib.Lazy;
import ch.verno.lib.New;
import ch.verno.rpc.client.helper.SortOrderMapper;
import ch.verno.rpc.rpc.RpcFactory;
import com.google.inject.Inject;
import com.vaadin.flow.data.provider.QuerySortOrder;
import jakarta.annotation.Nonnull;

import java.util.List;
import java.util.Optional;

public class ParticipantClient {

  @Nonnull private final Lazy<ParticipantResource> participantResource;

  @Inject
  public ParticipantClient(@Nonnull final RpcFactory rpcFactory) {
    this.participantResource = Lazy.of(() -> rpcFactory.create(ParticipantResource.class));
  }

  @Nonnull
  public Optional<ParticipantDto> getParticipantById(@Nonnull final Long id) {
    return participantResource.get().getParticipantById(id);
  }

  @Nonnull
  public List<ParticipantDto> getAllParticipants() {
    return participantResource.get().getParticipants();
  }

  @Nonnull
  public List<ParticipantDto> getParticipants(@Nonnull final ParticipantFilter filter,
                                              final int offset,
                                              final int limit,
                                              @Nonnull final List<QuerySortOrder> sortOrders) {
    final var orders = SortOrderMapper.toDto(sortOrders);
    return participantResource.get().getParticipants(filter, offset, limit, orders);
  }

  @Nonnull
  public List<ParticipantDto> getParticipants(@Nonnull final ParticipantFilter filter) {
    return participantResource.get().getParticipants(filter, 0, Integer.MAX_VALUE, New.list());
  }

  @Nonnull
  public ParticipantDto saveParticipant(@Nonnull final ParticipantDto dto) {
    return participantResource.get().saveParticipant(dto);
  }

  @Nonnull
  public DeleteResponse deleteParticipantById(@Nonnull final Long id) {
    return participantResource.get().deleteParticipantById(id);
  }

  @Nonnull
  public ParticipantDto enableParticipant(@Nonnull final Long id) {
    return participantResource.get().enableParticipant(id);
  }

  @Nonnull
  public ParticipantDto disableParticipant(@Nonnull final Long id) {
    return participantResource.get().disableParticipant(id);
  }

  @Nonnull
  public ParticipantDto addCourse(@Nonnull final Long participantId,
                                  @Nonnull final CourseDto course) {
    return participantResource.get().addCourse(participantId, course);
  }

  @Nonnull
  public ParticipantDto removeCourse(@Nonnull final Long participantId,
                                     @Nonnull final CourseDto course) {
    return participantResource.get().removeCourse(participantId, course);
  }

  @Nonnull
  public SaveResponse<ParticipantDto> apiSaveParticipant(@Nonnull final ParticipantDto dto) {
    return participantResource.get().apiSaveParticipant(dto);
  }
}
