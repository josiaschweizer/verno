package ch.verno.server.rpc.resource.participant;

import ch.verno.contract.dto.filter.ParticipantFilter;
import ch.verno.contract.dto.response.base.delete.DeleteResponse;
import ch.verno.contract.dto.response.base.save.SaveResponse;
import ch.verno.contract.dto.table.base.SortOrderDto;
import ch.verno.contract.dto.table.course.CourseDto;
import ch.verno.contract.dto.table.participant.ParticipantDto;
import ch.verno.contract.endpoint.participant.ParticipantResource;
import ch.verno.contract.rpc.RpcResource;
import ch.verno.lib.Lazy;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.bo.BoFactory;
import ch.verno.server.bo.table.participant.ParticipantBo;
import ch.verno.server.service.intern.table.participant.ParticipantService;
import jakarta.annotation.Nonnull;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
@RpcResource(ParticipantResource.class)
public class ParticipantResourceImpl implements ParticipantResource {

  @Nonnull private final Lazy<ParticipantBo> participantBo;
  @Nonnull private final Lazy<ParticipantService> participantService;

  public ParticipantResourceImpl(@Nonnull final ServerBean serverBean) {
    this.participantBo = Lazy.of(() -> serverBean.get(BoFactory.class).get(ParticipantBo.class));
    this.participantService = Lazy.of(() -> serverBean.get(ParticipantService.class));
  }

  @Override
  public Optional<ParticipantDto> getParticipantById(@Nonnull final Long id) {
    return participantService.get().findById(id);
  }

  @Nonnull
  @Override
  public List<ParticipantDto> getParticipants() {
    return participantService.get().findAll();
  }

  @Nonnull
  @Override
  public List<ParticipantDto> getParticipants(@Nonnull final ParticipantFilter filter,
                                              final int offset,
                                              final int limit,
                                              @Nonnull final List<SortOrderDto> sortOrders) {
    return participantService.get().findAll(filter, sortOrders, offset, limit);
  }

  @Nonnull
  @Override
  public SaveResponse<ParticipantDto> saveParticipant(@Nonnull final ParticipantDto dto) {
    return participantBo.get().saveParticipant(dto);
  }

  @Nonnull
  @Override
  public DeleteResponse deleteParticipantById(@Nonnull final Long id) {
    return participantService.get().deleteById(id);
  }

  @Nonnull
  @Override
  public ParticipantDto enableParticipant(@Nonnull final Long id) {
    return participantBo.get().enableParticipant(id);
  }

  @Nonnull
  @Override
  public ParticipantDto disableParticipant(@Nonnull final Long id) {
    return participantBo.get().disableParticipant(id);
  }

  @Nonnull
  @Override
  public ParticipantDto addCourse(@Nonnull final Long participantId,
                                  @Nonnull final CourseDto courseDto) {
    return participantBo.get().addCourse(participantId, courseDto);
  }

  @Nonnull
  @Override
  public ParticipantDto removeCourse(@Nonnull final Long participantId,
                                     @Nonnull final CourseDto courseDto) {
    return participantBo.get().removeCourse(participantId, courseDto);
  }
}
