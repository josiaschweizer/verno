package ch.verno.ui.verno.dashboard.io.widgets.instructor;

import ch.verno.common.db.service.IInstructorService;
import ch.verno.common.gate.GlobalGate;
import ch.verno.publ.Publ;
import ch.verno.ui.base.components.widget.VAAccordionWidgetBase;
import ch.verno.ui.verno.dashboard.io.dialog.export.ExportDialog;
import ch.verno.ui.verno.dashboard.io.dialog.importing.ImportDialog;
import ch.verno.ui.verno.instructor.InstructorsGrid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import jakarta.annotation.Nonnull;

public class InstructorWidget extends VAAccordionWidgetBase {

  @Nonnull private final GlobalGate globalGate;
  @Nonnull private final IInstructorService instructorService;
  private InstructorsGrid instructorGrid;

  public InstructorWidget(@Nonnull final GlobalGate globalGate) {
    this.globalGate = globalGate;
    this.instructorService = globalGate.getService(IInstructorService.class);

    build();
  }

  @Nonnull
  @Override
  protected String getTitleText() {
    return getTranslation("shared.instructor");
  }

  @Override
  protected void buildHeaderActions(@Nonnull final HorizontalLayout header) {
    final var importButton = createHeaderButton(
            getTranslation("shared.import"),
            VaadinIcon.DOWNLOAD,
            e -> {
              final var config = new InstructorImportConfig(globalGate);
              final var importDialog = new ImportDialog(
                      globalGate,
                      getTranslation("shared.import") + Publ.SPACE + getTranslation("shared.instructor"),
                      config
              );
              importDialog.addClosedListener(c -> refresh());
              importDialog.open();
            });
    final var exportButton = createHeaderButton(
            getTranslation("shared.export"),
            VaadinIcon.UPLOAD,
            e -> {
              final var config = new InstructorExportConfig(globalGate);
              final var exportDialog = new ExportDialog(globalGate, config);
              exportDialog.open();
            });

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
