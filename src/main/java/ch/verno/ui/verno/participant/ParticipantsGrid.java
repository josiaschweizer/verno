package ch.verno.ui.verno.participant;

import ch.verno.common.participant.ParticipantMapper;
import ch.verno.common.participant.ParticipantService;
import ch.verno.ui.base.toolbar.ViewToolbar;
import ch.verno.ui.lib.URL;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.Nonnull;

import java.io.Console;

@Route("participants")
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
    final var createButton = new Button("Create Participant");
    createButton.addClickListener(event -> {
      UI.getCurrent().navigate("participants/detail");
    });

    grid = new Grid<>();
    addColumn(ParticipantDto::getFirstName, "First Name");
    addColumn(ParticipantDto::getLastName, "Last Name");
    addColumn(ParticipantDto::getAge, "Age");
    addColumn(ParticipantDto::getEmail, "Email");
    addColumn(ParticipantDto::getPhone, "Phone");

    final var participants = participantService.getAllParticipants().stream().map(ParticipantMapper::toDto).toList();
    grid.setItems(participants);
    grid.addItemDoubleClickListener(event -> {
      final var url = URL.getDetailURL(this.getClass());
      System.out.println("url = " + url);
      final var participantId = event.getItem().getId();
      UI.getCurrent().navigate("participants/detail?id=" + participantId);
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
