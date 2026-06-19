package ch.verno.ui.verno.dashboard.io;

import ch.verno.ui.verno.dashboard.io.widgets.instructor.InstructorWidget;
import ch.verno.ui.verno.dashboard.io.widgets.participant.ParticipantWidget;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import jakarta.annotation.Nonnull;

public class ImportExportWidgetGroup extends VerticalLayout {

  @Inject
  public ImportExportWidgetGroup(@Nonnull final Injector injector) {
    setPadding(false);
    setMargin(false);
    setSpacing(false);
    setWidthFull();

    initUI(injector);
  }


  private void initUI(@Nonnull final Injector injector) {
    final var participant = injector.getInstance(ParticipantWidget.class);
    final var instructorWidget = injector.getInstance(InstructorWidget.class);

    add(participant, instructorWidget);
  }

}
