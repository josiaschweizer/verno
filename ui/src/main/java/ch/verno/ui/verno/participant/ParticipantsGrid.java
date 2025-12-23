package ch.verno.ui.verno.participant;

import ch.verno.common.db.dto.ParticipantDto;
import ch.verno.server.service.GenderService;
import ch.verno.server.service.ParticipantService;
import ch.verno.ui.base.components.toolbar.ViewToolbar;
import ch.verno.ui.lib.Routes;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.Nonnull;

@Route(Routes.PARTICIPANT)
@PageTitle("Participants Overview")
@Menu(order = 1, icon = "vaadin:users", title = "Participant Overview")
public class ParticipantsGrid extends VerticalLayout {

  @Nonnull
  private final ParticipantService participantService;
  @Nonnull
  private final GenderService genderService;

  private Grid<ParticipantDto> grid;

  public ParticipantsGrid(@Nonnull final ParticipantService participantService,
                          @Nonnull final GenderService genderService) {
    this.participantService = participantService;
    this.genderService = genderService;

    init();
  }

  private void init() {
    final var createButton = new Button("Create Participant");
    createButton.addClickListener(event -> {
      UI.getCurrent().navigate("participants/detail");
    });

    grid = new Grid<>();
    addColumn(ParticipantDto::firstName, "First Name");
    addColumn(ParticipantDto::lastName, "Last Name");
    addColumn(ParticipantDto::birthdate, "Age");
    addColumn(ParticipantDto::email, "Email");
    addColumn(ParticipantDto::phone, "Phone");

    final var participants = participantService.getAllParticipants();

    grid.setItems(participants);
    grid.addItemDoubleClickListener(event -> {
      final var url = Routes.getDetailURL(this.getClass());
      final var redirectURL = Routes.getURLWithId(url, event.getItem().id());
      UI.getCurrent().navigate(redirectURL);
    });

    setSizeFull();
    setPadding(false);
    setSpacing(false);

    add(new ViewToolbar("Participants Grid", createButton));
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
