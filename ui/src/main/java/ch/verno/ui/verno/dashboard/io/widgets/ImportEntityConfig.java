package ch.verno.ui.verno.dashboard.io.widgets;

import ch.verno.server.io.importing.dto.DbField;
import ch.verno.server.io.importing.dto.DbFieldTyped;
import jakarta.annotation.Nonnull;

import java.util.List;
import java.util.Map;

public interface ImportEntityConfig<T> {

  @Nonnull
  List<DbField<T>> getDbFields();

  List<DbFieldTyped<T, ?>> getTypedDbFields();

  boolean performImport(@Nonnull String fileToken, @Nonnull Map<String, String> mapping);

}
