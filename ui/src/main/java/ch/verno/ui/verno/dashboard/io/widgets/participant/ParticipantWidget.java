package ch.verno.ui.verno.dashboard.io.widgets.participant;

import ch.verno.common.gate.GlobalGate;
import ch.verno.publ.Publ;
import ch.verno.ui.base.components.notification.NotificationFactory;
import ch.verno.ui.base.components.widget.VAAccordionWidgetBase;
import ch.verno.ui.verno.dashboard.io.dialog.importing.ImportDialog;
import ch.verno.ui.verno.participant.ParticipantsGrid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import jakarta.annotation.Nonnull;
import org.jspecify.annotations.NonNull;

public class ParticipantWidget extends VAAccordionWidgetBase {

  @Nonnull private final GlobalGate globalGate;
  private ParticipantsGrid participantsGrid;

  public ParticipantWidget(@Nonnull final GlobalGate globalGate) {
    super();
    this.globalGate = globalGate;

    build();
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
              final var config = new ParticipantImportConfig(globalGate);
              final var importDialog = new ImportDialog(
                      globalGate,
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
//              final var config = new ParticipantExportConfig(globalGate);
//              final var exportDialog = new ExportDialog<>(globalGate, config);
//              exportDialog.open();
              NotificationFactory.showInfoNotification("Not implemented yet.");
              NotificationFactory.showWarningNotification("Not implemented yet.");
              NotificationFactory.showSuccessNotification("Not implemented yet.");
              NotificationFactory.showErrorNotification("Not implemented yet.");
            });

    header.add(importButton, exportButton);
  }

  @Override
  protected void initContent() {
    participantsGrid = new ParticipantsGrid(globalGate,
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
