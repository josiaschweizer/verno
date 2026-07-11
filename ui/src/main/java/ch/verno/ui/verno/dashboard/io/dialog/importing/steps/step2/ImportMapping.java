package ch.verno.ui.verno.dashboard.io.dialog.importing.steps.step2;

import ch.verno.common.server.io.importing.CsvColumn;
import ch.verno.lib.Lazy;
import ch.verno.lib.New;
import ch.verno.lib.Publ;
import ch.verno.rpc.client.file.CsvClient;
import ch.verno.ui.base.components.dialog.stepdialog.BaseDialogStep;
import ch.verno.ui.base.components.notification.NotificationFactory;
import ch.verno.ui.base.components.notification.inline.VAInlineNotification;
import ch.verno.ui.base.components.notification.inline.VAInlineNotificationTheme;
import ch.verno.ui.verno.dashboard.io.dto.ImportField;
import ch.verno.ui.verno.dashboard.io.widgets.ImportEntityConfig;
import ch.verno.ui.verno.dashboard.io.widgets.ImportResult;
import com.google.inject.Injector;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.jetbrains.annotations.NonNls;

import java.util.List;
import java.util.Map;

public class ImportMapping<T> extends BaseDialogStep {

  @NonNls public static final String RELATION_POST_FIX = Publ.SPACE + Publ.REQUIRED_STAR + "relation";

  @Nonnull private final Lazy<CsvClient> csvClient;
  @Nonnull private final ImportEntityConfig<T> entityConfig;

  private ImportColumnMappingPanel columnMappingPanel;

  @Nullable private String fileToken;
  @Nullable private Runnable onValidationChangedListener;

  public ImportMapping(@Nonnull final Injector injector,
                       @Nonnull final ImportEntityConfig<T> entityConfig) {
    this.csvClient = Lazy.of(() -> injector.getInstance(CsvClient.class));
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

    final var fileColumns = csvClient.get().resolveCsvSchema(fileToken);
    final List<String> csvHeaders = fileColumns.columns().stream().map(CsvColumn::name).toList();

    initUI(csvHeaders);
  }

  private void initUI(@Nonnull final List<String> csvHeaders) {
    final var allFields = collectAllFields();
    final var inlineNotification = createInfoInlineNotification();
    this.columnMappingPanel = new ImportColumnMappingPanel(csvHeaders, allFields);

    if (onValidationChangedListener != null) {
      columnMappingPanel.addValidationChangeListener(() -> onValidationChangedListener.run());
    }

    setSpacing(true);
    add(inlineNotification, columnMappingPanel);
  }

  @Nonnull
  private VAInlineNotification createInfoInlineNotification() {
    final var notification = new VAInlineNotification(VAInlineNotificationTheme.WARNING);
    notification.setTitle("Import Mapping"); //TODO translation
    notification.setDescription("DB-Felder, die mit *relation gekennzeichnet sind, verweisen auf andere Datensätze. Diese Felder sind anfälliger für Importfehler und sollten besonders sorgfältig zugeordnet werden."); //TODO translation
    return notification;
  }

  @Nonnull
  private List<ImportField> collectAllFields() {
    final var fields = New.<ImportField>arrayList();

    fields.addAll(getDbFields());
    fields.addAll(getTypedDbFields());
    fields.addAll(getNestedFields());
    fields.addAll(getRelationFields());

    return fields;
  }

  @Nonnull
  private List<ImportField> getDbFields() {
    final var fields = New.<ImportField>arrayList();
    for (final var field : entityConfig.getDbFields()) {
      fields.add(new ImportField(field.key(), getTranslation(field.label()), field.required()));
    }

    return fields;
  }

  @Nonnull
  private List<ImportField> getTypedDbFields() {
    final var fields = New.<ImportField>arrayList();
    for (final var field : entityConfig.getTypedDbFields()) {
      fields.add(new ImportField(field.key(), getTranslation(field.label()), field.required()));
    }

    return fields;
  }

  @Nonnull
  private List<ImportField> getNestedFields() {
    final var labelSpace = Publ.SPACE + Publ.MINUS + Publ.SPACE;

    final var fields = New.<ImportField>arrayList();
    for (final var nestedField : entityConfig.getNestedDbFields()) {
      final var prefix = nestedField.prefix() + Publ.DOT;

      for (final var field : nestedField.nestedStringFields()) {
        fields.add(new ImportField(
                prefix + field.key(),
                getTranslation(nestedField.label()) + labelSpace + getTranslation(field.label()),
                nestedField.required() && field.required()
        ));
      }

      for (final var field : nestedField.nestedTypedFields()) {
        fields.add(new ImportField(
                prefix + field.key(),
                getTranslation(nestedField.label()) + labelSpace + getTranslation(field.label()),
                nestedField.required() && field.required()
        ));
      }
    }

    return fields;
  }

  @Nonnull
  private List<ImportField> getRelationFields() {
    final var fields = New.<ImportField>arrayList();

    for (final var relationField : entityConfig.getRelationFields()) {
      fields.add(new ImportField(
              relationField.key(),
              getTranslation(relationField.label()) + RELATION_POST_FIX,
              relationField.required()
      ));
    }

    return fields;
  }

  public void setOnValidationChangedListener(@Nullable final Runnable listener) {
    this.onValidationChangedListener = listener;
    if (columnMappingPanel != null && listener != null) {
      columnMappingPanel.addValidationChangeListener(listener);
    }
  }

  @Nonnull
  public Map<String, String> getMapping() {
    if (!columnMappingPanel.isValid()) {
      NotificationFactory.showErrorNotification(getTranslation("base.bitte.alle.erforderlichen.felder.zuordnen"));
      return Map.of();
    }

    return columnMappingPanel.getMapping();
  }

  @Nonnull
  public ImportResult performImport() {
    if (fileToken == null) {
      return ImportResult.completeFailure();
    }

    return entityConfig.performImport(fileToken, getMapping());
  }

  @Override
  public boolean isValid() {
    return columnMappingPanel.isValid();
  }
}
