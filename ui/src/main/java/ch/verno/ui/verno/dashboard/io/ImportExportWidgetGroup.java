package ch.verno.ui.verno.dashboard.io;

import ch.verno.common.gate.GlobalGate;
import ch.verno.ui.verno.dashboard.io.widgets.instructor.InstructorWidget;
import ch.verno.ui.verno.dashboard.io.widgets.participant.ParticipantWidget;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import jakarta.annotation.Nonnull;

public class ImportExportWidgetGroup extends VerticalLayout {

  @Nonnull private final GlobalGate globalGate;

  public ImportExportWidgetGroup(@Nonnull final GlobalGate globalGate) {
    this.globalGate = globalGate;

    setPadding(false);
    setMargin(false);
    setSpacing(false);
    setWidthFull();

    initUI();
  }


  private void initUI() {
    final var participant = new ParticipantWidget(globalGate);
    final var instructorWidget = new InstructorWidget(globalGate);

    add(participant, instructorWidget);
  }

}
