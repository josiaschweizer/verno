package ch.verno.ui.verno.dashboard.io.widgets.instructor;

import ch.verno.lib.Publ;
import ch.verno.ui.base.components.widget.VAAccordionWidgetBase;
import ch.verno.ui.verno.dashboard.io.dialog.export.ExportDialog;
import ch.verno.ui.verno.dashboard.io.dialog.importing.ImportDialog;
import ch.verno.ui.verno.instructor.InstructorsGrid;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import jakarta.annotation.Nonnull;

public class InstructorWidget extends VAAccordionWidgetBase {

  @Nonnull private final Injector injector;
  private InstructorsGrid instructorGrid;

  @Inject
  public InstructorWidget(@Nonnull final Injector injector) {
    this.injector = injector;

    buildUI();
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
              final var config = injector.getInstance(InstructorImportConfig.class);
              final var importDialog = new ImportDialog(
                      injector,
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
              final var config = injector.getInstance(InstructorExportConfig.class);
              final var exportDialog = new ExportDialog<>(injector, config);
              exportDialog.open();
            });

    header.add(importButton, exportButton);
  }

  @Override
  protected void initContent() {
    instructorGrid = new InstructorsGrid(injector, false, false);
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
