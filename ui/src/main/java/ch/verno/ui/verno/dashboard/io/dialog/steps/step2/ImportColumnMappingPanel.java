package ch.verno.ui.verno.dashboard.io.dialog.steps.step2;


import ch.verno.server.io.importing.dto.DbField;
import ch.verno.ui.base.components.mapping.VABaseColumnMappingPanel;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ImportColumnMappingPanel<T> extends VABaseColumnMappingPanel<DbField<T>> {

  @Nonnull private final List<Runnable> validationChangeListeners;

  public ImportColumnMappingPanel(@Nonnull final List<String> csvColumns,
                                  @Nonnull final List<DbField<T>> dbFields) {
    super(csvColumns, dbFields, true, "shared.ignorieren");
    validationChangeListeners = new ArrayList<>();
  }

  @Nonnull
  @Override
  protected String getFieldKey(@Nonnull final DbField<T> field) {
    return field.key();
  }

  @Nonnull
  @Override
  protected String getFieldLabel(@Nonnull final DbField<T> field) {
    return field.label();
  }

  @Override
  protected void onMappingChanged(@Nonnull final String csvColumn,
                                  @Nullable final String oldFieldKey,
                                  @Nullable final String newFieldKey) {
    super.onMappingChanged(csvColumn, oldFieldKey, newFieldKey);
    notifyValidationChangeListeners();
  }

  public void addValidationChangeListener(@Nonnull final Runnable listener) {
    validationChangeListeners.add(listener);
  }

  private void notifyValidationChangeListeners() {
    validationChangeListeners.forEach(Runnable::run);
  }
}