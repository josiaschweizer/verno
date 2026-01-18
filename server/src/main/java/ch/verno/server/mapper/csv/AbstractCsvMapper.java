package ch.verno.server.mapper.csv;

import ch.verno.common.db.dto.base.BaseDto;
import ch.verno.common.file.CsvMapDto;
import ch.verno.server.io.importing.dto.DbField;
import ch.verno.server.io.importing.dto.DbFieldTyped;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.*;

public abstract class AbstractCsvMapper<T extends BaseDto> {

  @Nonnull
  protected abstract T newTarget();

  @Nonnull
  public final CsvMappingResult<T> map(@Nonnull final List<CsvMapDto> csvRows,
                                       @Nonnull final Map<String, String> mapping,
                                       @Nonnull final List<DbField<T>> stringFields,
                                       @Nonnull final List<DbFieldTyped<T, ?>> typedFields) {

    final var saveables = new ArrayList<T>();
    final var errors = new ArrayList<CsvMappingRowError>();

    final var stringFieldByKey = indexStringFields(stringFields);
    final var typedFieldByKey = indexTypedFields(typedFields);

    final var requiredKeys = new HashSet<String>();
    stringFields.stream().filter(DbField::required).forEach(f -> requiredKeys.add(f.key()));
    typedFields.stream().filter(DbFieldTyped::required).forEach(f -> requiredKeys.add(f.key()));

    for (int i = 0; i < csvRows.size(); i++) {
      final int rowIndex = i + 1;
      final var row = csvRows.get(i).row();

      final var target = newTarget();
      final var setKeys = new HashSet<String>();

      for (final var entry : mapping.entrySet()) {
        final var csvColumn = entry.getKey();
        final var dbKey = entry.getValue();

        final var raw = row.get(csvColumn);
        final var value = normalize(raw);
        if (value == null) {
          continue;
        }

        try {
          final var sf = stringFieldByKey.get(dbKey);
          if (sf != null) {
            sf.setter().accept(target, sf.normalizeValue(value));
            setKeys.add(dbKey);
            continue;
          }

          final var tf = typedFieldByKey.get(dbKey);
          if (tf != null) {
            applyTyped(tf, target, value);
            setKeys.add(dbKey);
          }

        } catch (Exception ex) {
          errors.add(new CsvMappingRowError(rowIndex,
                  "Fehler bei Feld '" + dbKey + "': " + ex.getMessage()));
        }
      }

      final var missing = requiredKeys.stream()
              .filter(k -> !setKeys.contains(k))
              .toList();

      if (!missing.isEmpty()) {
        errors.add(new CsvMappingRowError(rowIndex,
                "Pflichtfelder fehlen: " + String.join(", ", missing)));
        continue;
      }

      saveables.add(target);
    }

    return new CsvMappingResult<>(saveables, errors);
  }

  @SuppressWarnings("unchecked")
  private static <T> void applyTyped(@Nonnull final DbFieldTyped<T, ?> field,
                                     @Nonnull final T target,
                                     @Nonnull final String raw) {
    final var parsed = ((DbFieldTyped<T, Object>) field).parser().apply(raw);
    ((DbFieldTyped<T, Object>) field).setter().accept(target, parsed);
  }

  @Nonnull
  private static <T> Map<String, DbField<T>> indexStringFields(@Nonnull final List<DbField<T>> fields) {
    final var map = new HashMap<String, DbField<T>>();
    for (var f : fields) {
      map.put(f.key(), f);
    }
    return map;
  }

  @Nonnull
  private static <T> Map<String, DbFieldTyped<T, ?>> indexTypedFields(@Nonnull final List<DbFieldTyped<T, ?>> fields) {
    final var map = new HashMap<String, DbFieldTyped<T, ?>>();
    for (var f : fields) {
      map.put(f.key(), f);
    }
    return map;
  }

  @Nullable
  protected final String normalize(@Nullable final String s) {
    if (s == null) return null;
    final var t = s.trim();
    return t.isEmpty() ? null : t;
  }
}