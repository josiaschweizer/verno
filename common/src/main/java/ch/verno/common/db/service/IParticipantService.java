package ch.verno.common.db.service;

import ch.verno.common.db.dto.table.CourseDto;
import ch.verno.common.db.dto.table.ParticipantDto;
import ch.verno.common.db.filter.ParticipantFilter;
import com.vaadin.flow.data.provider.QuerySortOrder;
import jakarta.annotation.Nonnull;

import java.util.List;
import java.util.Optional;

public interface IParticipantService {

  @Nonnull
  ParticipantDto createParticipant(@Nonnull ParticipantDto participant);

  @Nonnull
  ParticipantDto updateParticipant(@Nonnull ParticipantDto participant);

  ParticipantDto disableParticipant(@Nonnull final ParticipantDto participant,
                                    final boolean disabled);

  @Nonnull
  ParticipantDto getParticipantById(@Nonnull Long id);

  @Nonnull
  Optional<ParticipantDto> getParticipantByEmail(@Nonnull String email);

  @Nonnull
  List<ParticipantDto> getAllParticipants();

  @Nonnull
  List<ParticipantDto> findParticipants(@Nonnull final ParticipantFilter filter);

  @Nonnull
  List<ParticipantDto> findParticipants(@Nonnull ParticipantFilter filter,
                                        int offset,
                                        int limit,
                                        @Nonnull List<QuerySortOrder> sortOrders);

  int countParticipants(@Nonnull ParticipantFilter filter);

  @Nonnull
  List<ParticipantDto> findParticipantsByCourse(@Nonnull CourseDto course);
}
