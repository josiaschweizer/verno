package ch.verno.ui.verno.dashboard.io;

import ch.verno.common.gate.GlobalInterface;
import ch.verno.ui.verno.dashboard.io.widgets.instructor.InstructorWidget;
import ch.verno.ui.verno.dashboard.io.widgets.participant.ParticipantWidget;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import jakarta.annotation.Nonnull;

public class ImportExportWidgetGroup extends VerticalLayout {

  @Nonnull private final GlobalInterface globalInterface;

  public ImportExportWidgetGroup(@Nonnull final GlobalInterface globalInterface) {
    this.globalInterface = globalInterface;

    setPadding(false);
    setMargin(false);
    setSpacing(false);
    setWidthFull();

    initUI();
  }


  private void initUI() {
    final var participant = new ParticipantWidget(globalInterface);
    final var instructorWidget = new InstructorWidget(globalInterface);

    add(participant, instructorWidget);
  }

}
