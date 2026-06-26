package ch.verno.ui.verno.dashboard.io.widgets.participant;

import ch.verno.lib.Publ;
import ch.verno.ui.base.components.widget.VAAccordionWidgetBase;
import ch.verno.ui.verno.dashboard.io.dialog.export.ExportDialog;
import ch.verno.ui.verno.dashboard.io.dialog.importing.ImportDialog;
import ch.verno.ui.verno.participant.ParticipantsGrid;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.jspecify.annotations.NonNull;

public class ParticipantWidget extends VAAccordionWidgetBase {

  @Nonnull private final Injector injector;
  @Nullable private ParticipantsGrid participantsGrid;

  @Inject
  public ParticipantWidget(@Nonnull final Injector injector) {
    super();
    this.injector = injector;

    buildUI();
  }

  @Nonnull
  @Override
  protected String getTitleText() {
    return getTranslation("participant.participant");
  }

  @Override
  protected void buildHeaderActions(@NonNull final HorizontalLayout header) {
    final var importButton = createHeaderButton(
            getTranslation("shared.import"),
            VaadinIcon.DOWNLOAD,
            e -> {
              final var config = injector.getInstance(ParticipantImportConfig.class);
              final var importDialog = new ImportDialog(
                      injector,
                      getTranslation("shared.import") + Publ.SPACE + getTranslation("participant.participant"),
                      config
              );
              importDialog.addClosedListener(close -> refresh());
              importDialog.open();
            });
    final var exportButton = createHeaderButton(
            getTranslation("shared.export"),
            VaadinIcon.UPLOAD,
            e -> {
              final var config = injector.getInstance(ParticipantExportConfig.class);
              final var exportDialog = new ExportDialog<>(injector, config);
              exportDialog.open();
            });

    header.add(importButton, exportButton);
  }

  @Override
  protected void initContent() {
    participantsGrid = new ParticipantsGrid(
            injector,
            false,
            false);
    participantsGrid.getGrid().setAllRowsVisible(true);
    participantsGrid.setWidthFull();
    add(participantsGrid);
  }

  protected void refresh() {
    if (participantsGrid == null) {
      return;
    }

    participantsGrid.setFilter(participantsGrid.getFilter());
  }
}
