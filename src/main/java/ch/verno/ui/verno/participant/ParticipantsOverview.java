package ch.verno.ui.verno.participant;

import ch.verno.domain.participant.ParticipantService;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.Nonnull;

@Route("participants-overview")
@PageTitle("Participants Overview")
public class ParticipantsOverview extends VerticalLayout {

  @Nonnull
  private final ParticipantService participantService;

  private Grid<Participant> grid;

  public ParticipantsOverview(@Nonnull final ParticipantService participantService) {
    this.participantService = participantService;

    initUI();
  }

  private void initUI() {
    grid = new Grid<>();

    this.add(grid);
  }

}
