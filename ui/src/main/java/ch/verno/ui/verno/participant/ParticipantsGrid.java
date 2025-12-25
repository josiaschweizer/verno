package ch.verno.ui.verno.participant;

import ch.verno.common.db.dto.ParticipantDto;
import ch.verno.server.service.ParticipantService;
import ch.verno.ui.base.grid.BaseOverviewGrid;
import ch.verno.ui.lib.Routes;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.ItemDoubleClickEvent;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.Nonnull;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Route(Routes.PARTICIPANTS)
@PageTitle("Participants Overview")
@Menu(order = 1, icon = "vaadin:users", title = "Participants Overview")
public class ParticipantsGrid extends BaseOverviewGrid<ParticipantDto> {

  @Nonnull
  private final ParticipantService participantService;

  public ParticipantsGrid(@Nonnull final ParticipantService participantService) {
    this.participantService = participantService;

    initGrid();
  }

  @Override
  protected void onGridItemDoubleClick(@Nonnull final ItemDoubleClickEvent<ParticipantDto> event) {
    final var url = Routes.getDetailURL(this.getClass());
    final var redirectURL = Routes.getURLWithId(url, event.getItem().getId());
    UI.getCurrent().navigate(redirectURL);
  }

  @Nonnull
  @Override
  protected List<ParticipantDto> fetchItems() {
    return participantService.getAllParticipants();
  }

  @Nonnull
  @Override
  protected String getGridObjectName() {
    return "Participant";
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
    return columnsMap;
  }


}
