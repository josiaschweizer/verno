package ch.verno.ui.verno.participant;

import ch.verno.common.db.dto.ParticipantDto;
import ch.verno.common.util.Publ;
import ch.verno.common.util.VernoConstants;
import ch.verno.server.service.ParticipantService;
import ch.verno.ui.base.grid.BaseOverviewGrid;
import ch.verno.ui.lib.Routes;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.Nonnull;
import jakarta.annotation.security.PermitAll;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@PermitAll
@Route(Routes.PARTICIPANTS)
@PageTitle("Participants Overview")
@Menu(order = 1, icon = "vaadin:users", title = "Participants Overview")
public class ParticipantsGrid extends BaseOverviewGrid<ParticipantDto> {

  @Nonnull
  private final ParticipantService participantService;

  public ParticipantsGrid(@Nonnull final ParticipantService participantService) {
    this.participantService = participantService;
  }

  @Nonnull
  @Override
  protected List<ParticipantDto> fetchItems() {
    return participantService.getAllParticipants();
  }

  @Nonnull
  @Override
  protected String getGridObjectName() {
    return VernoConstants.PARTICIPANT;
  }

  @Override
  protected String getDetailPageRoute() {
    return Routes.createUrlFromUrlSegments(Routes.PARTICIPANTS, Routes.DETAIL);
  }

  @Nonnull
  @Override
  protected Map<ValueProvider<ParticipantDto, Object>, String> getColumns() {
    final var columnsMap = new LinkedHashMap<ValueProvider<ParticipantDto, Object>, String>();
    columnsMap.put(ParticipantDto::getFirstName, "First Name");
    columnsMap.put(ParticipantDto::getLastName, "Last Name");
    columnsMap.put(ParticipantDto::getBirthdate, "Age");
    columnsMap.put(ParticipantDto::getEmail, "Email");
    columnsMap.put(ParticipantDto::getPhoneString, "Phone");
    columnsMap.put(ParticipantDto::getNote, "Note");
    columnsMap.put(dto -> dto.getCourseLevel().getName(), "Course Level");
    columnsMap.put(dto -> dto.getCourse() != null ? dto.getCourse().displayName() : Publ.EMPTY_STRING, "Course");
    columnsMap.put(dto -> dto.getParentOne().displayName(), "Parent One");
    columnsMap.put(dto -> dto.getParentTwo().displayName(), "Parent Two");
    columnsMap.put(dto -> dto.getAddress().getFullAddressAsString(), "Address");
    return columnsMap;
  }


}
