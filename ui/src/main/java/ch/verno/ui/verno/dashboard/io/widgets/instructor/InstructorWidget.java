package ch.verno.ui.verno.dashboard.io.widgets.instructor;

import ch.verno.common.db.service.IInstructorService;
import ch.verno.common.gate.VernoApplicationGate;
import ch.verno.publ.Publ;
import ch.verno.ui.base.components.notification.NotificationFactory;
import ch.verno.ui.base.components.widget.VAAccordionWidgetBase;
import ch.verno.ui.verno.dashboard.io.dialog.ImportDialog;
import ch.verno.ui.verno.instructor.InstructorsGrid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import jakarta.annotation.Nonnull;
import org.jspecify.annotations.NonNull;

public class InstructorWidget extends VAAccordionWidgetBase {

  @Nonnull private final VernoApplicationGate vernoApplicationGate;
  @Nonnull private final IInstructorService instructorService;
  private InstructorsGrid instructorGrid;

  public InstructorWidget(@Nonnull final VernoApplicationGate vernoApplicationGate) {
    this.vernoApplicationGate = vernoApplicationGate;
    this.instructorService = vernoApplicationGate.getService(IInstructorService.class);

    build();
  }

  @Nonnull
  @Override
  protected String getTitleText() {
    return getTranslation("shared.instructor");
  }

  @Override
  protected void buildHeaderActions(@NonNull final HorizontalLayout header) {
    final var importButton = createHeaderButton(
            getTranslation("shared.import"),
            VaadinIcon.DOWNLOAD,
            e -> {
              final var config = new InstructorImportConfig(vernoApplicationGate);
              final var importDialog = new ImportDialog(
                      vernoApplicationGate,
                      getTranslation("shared.import") + Publ.SPACE + getTranslation("shared.instructor"),
                      config
              );
              importDialog.addClosedListener(c -> refresh());
              importDialog.open();
            });
    final var exportButton = createHeaderButton(
            getTranslation("shared.export"),
            VaadinIcon.UPLOAD,
            e -> NotificationFactory.showInfoNotification("This feature is not implemented yet."));

    header.add(importButton, exportButton);
  }

  @Override
  protected void initContent() {
    instructorGrid = new InstructorsGrid(instructorService, false, false);
    instructorGrid.getGrid().setAllRowsVisible(true);
    add(instructorGrid);
  }

  protected void refresh() {
    if (instructorGrid == null) {
      return;
    }

    instructorGrid.setFilter(instructorGrid.getFilter());
  }
}
