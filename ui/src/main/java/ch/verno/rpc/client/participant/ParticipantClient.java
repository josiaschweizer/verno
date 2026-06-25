package ch.verno.rpc.client.participant;

import ch.verno.contract.dto.filter.ParticipantFilter;
import ch.verno.contract.dto.result.base.SaveResult;
import ch.verno.contract.dto.table.participant.ParticipantDto;
import ch.verno.contract.endpoint.participant.ParticipantResource;
import ch.verno.lib.Lazy;
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
    return participantResource.get().getAllParticipants();
  }

  @Nonnull
  public List<ParticipantDto> getAllParticipants(@Nonnull final ParticipantFilter filter,
                                                 final int offset,
                                                 final int limit,
                                                 List<QuerySortOrder> sortOrders) {
    final var orders = SortOrderMapper.toDto(sortOrders);
    return participantResource.get().getAllParticipants(filter, offset, limit, orders);
  }

  @Nonnull
  public SaveResult<ParticipantDto> saveParticipant(@Nonnull final ParticipantDto dto) {
    return participantResource.get().saveParticipant(dto);
  }

  public boolean deleteParticipantById(@Nonnull final Long id) {
    return participantResource.get().deleteParticipantById(id);
  }

}
