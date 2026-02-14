package ch.verno.server.mapper.csv;

import ch.verno.common.db.dto.base.BaseDto;
import ch.verno.common.api.dto.internal.file.temp.CsvMapDto;
import ch.verno.server.io.importing.dto.DbField;
import ch.verno.server.io.importing.dto.DbFieldNested;
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
    return map(csvRows, mapping, stringFields, typedFields, Collections.emptyList());
  }

  @Nonnull
  public final CsvMappingResult<T> map(@Nonnull final List<CsvMapDto> csvRows,
                                       @Nonnull final Map<String, String> mapping,
                                       @Nonnull final List<DbField<T>> stringFields,
                                       @Nonnull final List<DbFieldTyped<T, ?>> typedFields,
                                       @Nonnull final List<DbFieldNested<T, ?>> nestedFields) {

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

      for (final var nestedField : nestedFields) {
        try {
          processAndApplyNestedField(nestedField, target, mapping, row, rowIndex, errors);
        } catch (Exception ex) {
          errors.add(new CsvMappingRowError(rowIndex,
                  "Fehler bei verschachteltem Feld '" + nestedField.prefix() + "': " + ex.getMessage()));
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

  @Nullable
  private <N> N processNestedField(@Nonnull final DbFieldNested<T, N> nestedField,
                                   @Nonnull final Map<String, String> mapping,
                                   @Nonnull final Map<String, String> row,
                                   final int rowIndex,
                                   @Nonnull final List<CsvMappingRowError> errors) {

    final var prefix = nestedField.prefix() + ".";
    final var nestedEntity = nestedField.nestedFactory().get();

    final var stringFieldByKey = indexStringFields(nestedField.nestedStringFields());
    final var typedFieldByKey = indexTypedFields(nestedField.nestedTypedFields());

    final var setKeys = new HashSet<String>();
    boolean hasAnyValue = false;

    for (final var entry : mapping.entrySet()) {
      final var csvColumn = entry.getKey();
      final var dbKey = entry.getValue();

      if (!dbKey.startsWith(prefix)) {
        continue;
      }

      final var nestedKey = dbKey.substring(prefix.length());
      final var raw = row.get(csvColumn);
      final var value = normalize(raw);

      if (value == null) {
        continue;
      }

      hasAnyValue = true;

      try {
        final var sf = stringFieldByKey.get(nestedKey);
        if (sf != null) {
          sf.setter().accept(nestedEntity, sf.normalizeValue(value));
          setKeys.add(nestedKey);
          continue;
        }

        final var tf = typedFieldByKey.get(nestedKey);
        if (tf != null) {
          applyTyped(tf, nestedEntity, value);
          setKeys.add(nestedKey);
        }
      } catch (Exception ex) {
        errors.add(new CsvMappingRowError(rowIndex,
                "Fehler bei Feld '" + dbKey + "': " + ex.getMessage()));
      }
    }

    // If no values were set, return null
    if (!hasAnyValue) {
      return null;
    }

    // Check required fields for nested entity
    if (nestedField.required()) {
      final var requiredKeys = new HashSet<String>();
      nestedField.nestedStringFields().stream()
              .filter(DbField::required)
              .forEach(f -> requiredKeys.add(f.key()));
      nestedField.nestedTypedFields().stream()
              .filter(DbFieldTyped::required)
              .forEach(f -> requiredKeys.add(f.key()));

      final var missing = requiredKeys.stream()
              .filter(k -> !setKeys.contains(k))
              .toList();

      if (!missing.isEmpty()) {
        errors.add(new CsvMappingRowError(rowIndex,
                "Pflichtfelder für '" + nestedField.prefix() + "' fehlen: " + String.join(", ", missing)));
        return null;
      }
    }

    return nestedEntity;
  }

  private <N> void processAndApplyNestedField(@Nonnull final DbFieldNested<T, N> nestedField,
                                              @Nonnull final T target,
                                              @Nonnull final Map<String, String> mapping,
                                              @Nonnull final Map<String, String> row,
                                              final int rowIndex,
                                              @Nonnull final List<CsvMappingRowError> errors) {
    final var nestedEntity = processNestedField(nestedField, mapping, row, rowIndex, errors);
    if (nestedEntity != null) {
      nestedField.setter().accept(target, nestedEntity);
    }
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
    for (var field : fields) {
      map.put(field.key(), field);
    }

    return map;
  }

  @Nonnull
  private static <T> Map<String, DbFieldTyped<T, ?>> indexTypedFields(@Nonnull final List<DbFieldTyped<T, ?>> fields) {
    final var map = new HashMap<String, DbFieldTyped<T, ?>>();
    for (final var field : fields) {
      map.put(field.key(), field);
    }

    return map;
  }


  @Nullable
  protected final String normalize(@Nullable final String s) {
    if (s == null) {
      return null;
    }

    final var trimed = s.trim();
    return trimed.isEmpty() ? null : trimed;
  }
}