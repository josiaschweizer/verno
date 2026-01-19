package ch.verno.ui.verno.dashboard.io.dialog.importing.steps.step2;

import ch.verno.common.gate.GlobalGate;
import ch.verno.common.server.ServerGate;
import ch.verno.common.server.io.importing.CsvColumn;
import ch.verno.ui.base.components.notification.NotificationFactory;
import ch.verno.ui.base.dialog.stepdialog.BaseDialogStep;
import ch.verno.ui.verno.dashboard.io.dto.ImportField;
import ch.verno.ui.verno.dashboard.io.widgets.ImportEntityConfig;
import ch.verno.ui.verno.dashboard.io.widgets.ImportResult;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ImportMapping<T> extends BaseDialogStep {

  @Nonnull private final GlobalGate globalGate;
  @Nonnull private final ImportEntityConfig<T> entityConfig;

  private ImportColumnMappingPanel panel;

  @Nullable private String fileToken;
  @Nullable private Runnable onValidationChangedListener;

  public ImportMapping(@Nonnull final GlobalGate globalGate,
                       @Nonnull final ImportEntityConfig<T> entityConfig) {
    this.globalGate = globalGate;
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

    final var serverGate = globalGate.getService(ServerGate.class);
    final var fileColumns = serverGate.resolveCsvSchema(fileToken);
    final List<String> csvHeaders = fileColumns.columns().stream().map(CsvColumn::name).toList();

    initUI(csvHeaders);
  }

  private void initUI(@Nonnull final List<String> csvHeaders) {
    final var allFields = collectAllFields();
    panel = new ImportColumnMappingPanel(csvHeaders, allFields);

    if (onValidationChangedListener != null) {
      panel.addValidationChangeListener(() -> onValidationChangedListener.run());
    }

    add(panel);
  }

  @Nonnull
  private List<ImportField> collectAllFields() {
    final var fields = new ArrayList<ImportField>();

    for (final var field : entityConfig.getDbFields()) {
      fields.add(new ImportField(field.key(), getTranslation(field.label()), field.required()));
    }

    for (final var field : entityConfig.getTypedDbFields()) {
      fields.add(new ImportField(field.key(), getTranslation(field.label()), field.required()));
    }

    for (final var nestedField : entityConfig.getNestedDbFields()) {
      final var prefix = nestedField.prefix() + ".";

      for (final var field : nestedField.nestedStringFields()) {
        fields.add(new ImportField(
                prefix + field.key(),
                getTranslation(nestedField.label()) + " - " + getTranslation(field.label()),
                nestedField.required() && field.required()
        ));
      }

      for (final var field : nestedField.nestedTypedFields()) {
        fields.add(new ImportField(
                prefix + field.key(),
                getTranslation(nestedField.label()) + " - " + getTranslation(field.label()),
                nestedField.required() && field.required()
        ));
      }
    }

    return fields;
  }

  public void setOnValidationChangedListener(@Nullable final Runnable listener) {
    this.onValidationChangedListener = listener;
    if (panel != null && listener != null) {
      panel.addValidationChangeListener(listener);
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

  public ImportResult performImport() {
    if (fileToken == null) {
      return ImportResult.completeFailure();
    }

    return entityConfig.performImport(fileToken, getMapping());
  }

  @Override
  public boolean isValid() {
    return panel.isValid();
  }
}
