package ch.verno.ui.verno.participant;

import ch.verno.common.db.dto.ParticipantDto;
import ch.verno.server.service.ParticipantService;
import ch.verno.ui.base.components.toolbar.ViewToolbarFactory;
import ch.verno.ui.lib.Routes;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.Nonnull;

@Route(Routes.PARTICIPANTS)
@PageTitle("Participants Overview")
@Menu(order = 1, icon = "vaadin:users", title = "Participant Overview")
public class ParticipantsGrid extends VerticalLayout {

  @Nonnull
  private final ParticipantService participantService;

  private Grid<ParticipantDto> grid;

  public ParticipantsGrid(@Nonnull final ParticipantService participantService) {
    this.participantService = participantService;

    init();
  }

  private void init() {


    grid = new Grid<>();
    addColumn(ParticipantDto::getFirstName, "First Name");
    addColumn(ParticipantDto::getLastName, "Last Name");
    addColumn(ParticipantDto::getBirthdate, "Age");
    addColumn(ParticipantDto::getEmail, "Email");
    addColumn(ParticipantDto::getPhone, "Phone");

    final var participants = participantService.getAllParticipants();

    grid.setItems(participants);
    grid.addItemDoubleClickListener(event -> {
      final var url = Routes.getDetailURL(this.getClass());
      final var redirectURL = Routes.getURLWithId(url, event.getItem().getId());
      UI.getCurrent().navigate(redirectURL);
    });

    setSizeFull();
    setPadding(false);
    setSpacing(false);

    add(ViewToolbarFactory.createGridToolbar("Participant"));
    add(grid);
  }

  private void addColumn(@Nonnull final ValueProvider<ParticipantDto, Object> valueProvider,
                         @Nonnull final String header) {
    grid.addColumn(valueProvider)
        .setHeader(header)
        .setResizable(true)
        .setAutoWidth(true);
  }

}
