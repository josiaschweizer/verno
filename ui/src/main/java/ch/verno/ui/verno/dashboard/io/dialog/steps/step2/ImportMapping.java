package ch.verno.ui.verno.dashboard.io.dialog.steps.step2;

import ch.verno.common.gate.VernoApplicationGate;
import ch.verno.common.server.ServerGate;
import ch.verno.common.server.io.importing.CsvColumn;
import ch.verno.ui.base.components.notification.NotificationFactory;
import ch.verno.ui.base.dialog.stepdialog.BaseDialogStep;
import ch.verno.ui.verno.dashboard.io.widgets.ImportEntityConfig;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.List;
import java.util.Map;

public class ImportMapping<T> extends BaseDialogStep {

  @Nonnull private final VernoApplicationGate vernoApplicationGate;
  @Nonnull private final ImportEntityConfig<T> entityConfig;

  private ImportColumnMappingPanel<T> panel;

  @Nullable private String fileToken;
  @Nullable private Runnable onValidationChangedListener;

  public ImportMapping(@Nonnull final VernoApplicationGate vernoApplicationGate,
                       @Nonnull final ImportEntityConfig<T> entityConfig) {
    this.vernoApplicationGate = vernoApplicationGate;
    this.entityConfig = entityConfig;
    setSizeFull();
    setPadding(false);
    setSpacing(false);
  }

  public void setFileToken(@Nonnull final String fileToken) {
    this.fileToken = fileToken;
    loadAndInitializeMapping();
  }

  private void loadAndInitializeMapping() {
    if (fileToken == null) {
      return;
    }

    removeAll();

    final var serverGate = vernoApplicationGate.getService(ServerGate.class);
    final var fileColumns = serverGate.resolveCsvSchema(fileToken);
    final List<String> csvHeaders = fileColumns.columns().stream().map(CsvColumn::name).toList();

    initUI(csvHeaders);
  }

  private void initUI(@Nonnull final List<String> csvHeaders) {
    panel = new ImportColumnMappingPanel<>(csvHeaders, entityConfig.getDbFields());

    if (onValidationChangedListener != null) {
      panel.addValidationChangeListener(() -> onValidationChangedListener.run());
    }

    add(panel);
  }

  public void setOnValidationChangedListener(@Nullable final Runnable listener) {
    this.onValidationChangedListener = listener;
    if (panel != null) {
      panel.addValidationChangeListener(() -> listener.run());
    }
  }

  @Nonnull
  public Map<String, String> getMapping() {
    if (!panel.isValid()) {
      NotificationFactory.showErrorNotification("Bitte alle erforderlichen Felder zuordnen.");
      return Map.of();
    }

    return panel.getMapping();
  }

  public boolean performImport() {
    if (fileToken == null) {
      return false;
    }

    return entityConfig.performImport(fileToken, getMapping());
  }

  @Override
  public boolean isValid() {
    return panel.isValid();
  }
}
