package ch.verno.ui.verno.dashboard.io.widgets;

import ch.verno.server.io.importing.dto.DbField;
import ch.verno.server.io.importing.dto.DbFieldNested;
import ch.verno.server.io.importing.dto.DbFieldTyped;
import jakarta.annotation.Nonnull;
import org.jetbrains.annotations.NonNls;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public interface ImportEntityConfig<T> {

  @NonNls String IMPORT_ERROR_COLUMN_NAME = "import_error";

  @Nonnull
  List<DbField<T>> getDbFields();

  @Nonnull
  List<DbFieldTyped<T, ?>> getTypedDbFields();

  @Nonnull
  default List<DbFieldNested<T, ?>> getNestedDbFields() {
    return Collections.emptyList();
  }

  @Nonnull
  ImportResult performImport(@Nonnull String fileToken, @Nonnull Map<String, String> mapping);

  @Nonnull
  default String getImportErrorColumnName() {
    return IMPORT_ERROR_COLUMN_NAME;
  }

}
