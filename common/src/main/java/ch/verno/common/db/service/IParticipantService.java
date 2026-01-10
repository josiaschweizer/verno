package ch.verno.common.db.service;

import ch.verno.common.db.dto.CourseDto;
import ch.verno.common.db.dto.ParticipantDto;
import ch.verno.common.db.filter.ParticipantFilter;
import com.vaadin.flow.data.provider.QuerySortOrder;
import jakarta.annotation.Nonnull;

import java.util.List;

public interface IParticipantService {

  @Nonnull
  ParticipantDto createParticipant(@Nonnull ParticipantDto participantEntity);

  @Nonnull
  ParticipantDto updateParticipant(@Nonnull ParticipantDto participantEntity);

  @Nonnull
  ParticipantDto getParticipantById(@Nonnull Long id);

  @Nonnull
  List<ParticipantDto> getAllParticipants();

  @Nonnull
  List<ParticipantDto> findParticipants(@Nonnull ParticipantFilter filter,
                                        int offset,
                                        int limit,
                                        @Nonnull List<QuerySortOrder> sortOrders);

  int countParticipants(@Nonnull ParticipantFilter filter);

  @Nonnull
  List<ParticipantDto> findParticipantsByCourse(@Nonnull CourseDto course);
}
