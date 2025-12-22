package ch.verno.ui.verno.participant;

import ch.verno.common.gender.GenderMapper;
import ch.verno.common.gender.GenderService;
import ch.verno.common.participant.ParticipantMapper;
import ch.verno.common.participant.ParticipantService;
import ch.verno.common.base.lib.Routes;
import ch.verno.ui.base.components.toolbar.ViewToolbar;
import ch.verno.ui.verno.participant.dto.ParticipantDto;
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
    addColumn(ParticipantDto::getFirstName, "First Name");
    addColumn(ParticipantDto::getLastName, "Last Name");
    addColumn(ParticipantDto::getBirthdate, "Age");
    addColumn(ParticipantDto::getEmail, "Email");
    addColumn(ParticipantDto::getPhoneNumber, "Phone");

    final var participants = participantService.getAllParticipants().stream().map(entity -> {
      final var genderById = genderService.getGenderById(entity.getGenderId());
      return ParticipantMapper.toDto(entity, GenderMapper.toDto(genderById));
    }).toList();

    grid.setItems(participants);
    grid.addItemDoubleClickListener(event -> {
      final var url = Routes.getDetailURL(this.getClass());
      final var redirectURL = Routes.getURLWithId(url, event.getItem().getId());
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
