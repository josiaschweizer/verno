package ch.verno.contract.endpoint.participant;

import ch.verno.contract.dto.filter.ParticipantFilter;
import ch.verno.contract.dto.result.base.SaveResult;
import ch.verno.contract.dto.table.base.SortOrderDto;
import ch.verno.contract.dto.table.participant.ParticipantDto;
import ch.verno.contract.rpc.RpcEndpoint;
import jakarta.annotation.Nonnull;

import java.util.List;
import java.util.Optional;

@RpcEndpoint
public interface ParticipantResource {

  Optional<ParticipantDto> getParticipantById(@Nonnull Long id);

  @Nonnull
  List<ParticipantDto> getAllParticipants();

  @Nonnull
  List<ParticipantDto> getAllParticipants(@Nonnull ParticipantFilter filter,
                                          int offset,
                                          int limit,
                                          @Nonnull List<SortOrderDto> sortOrders);

  @Nonnull
  SaveResult<ParticipantDto> saveParticipant(@Nonnull ParticipantDto dto);

  boolean deleteParticipantById(@Nonnull Long id);


}
