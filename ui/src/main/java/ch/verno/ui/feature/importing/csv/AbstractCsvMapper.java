package ch.verno.ui.feature.importing.csv;

import ch.verno.common.io.importing.DbField;
import ch.verno.common.io.importing.DbFieldNested;
import ch.verno.common.io.importing.DbFieldRelation;
import ch.verno.common.io.importing.DbFieldTyped;
import ch.verno.contract.dto.file.temp.CsvMapDto;
import ch.verno.contract.dto.table.base.BaseDto;
import ch.verno.lib.Publ;
import ch.verno.lib.sanitize.StringSanitizer;
import ch.verno.ui.i18n.TranslationHelper;
import com.google.inject.Injector;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.*;

public abstract class AbstractCsvMapper<T extends BaseDto> {

  @Nonnull private final Injector injector;

  public AbstractCsvMapper(@Nonnull final Injector injector) {
    this.injector = injector;
  }

  @Nonnull
  protected abstract T newTarget();

  @Nonnull
  public final CsvMappingResult<T> map(@Nonnull final List<CsvMapDto> csvRows,
                                       @Nonnull final Map<String, String> mapping,
                                       @Nonnull final List<DbField<T>> stringFields,
                                       @Nonnull final List<DbFieldTyped<T, ?>> typedFields) {
    return map(csvRows, mapping, stringFields, typedFields, Collections.emptyList(), Collections.emptyList());
  }

  @Nonnull
  public final CsvMappingResult<T> map(@Nonnull final List<CsvMapDto> csvRows,
                                       @Nonnull final Map<String, String> mapping,
                                       @Nonnull final List<DbField<T>> stringFields,
                                       @Nonnull final List<DbFieldTyped<T, ?>> typedFields,
                                       @Nonnull final List<DbFieldNested<T, ?>> nestedFields) {
    return map(csvRows, mapping, stringFields, typedFields, nestedFields, Collections.emptyList());
  }

  @Nonnull
  public final CsvMappingResult<T> map(@Nonnull final List<CsvMapDto> csvRows,
                                       @Nonnull final Map<String, String> mapping,
                                       @Nonnull final List<DbField<T>> stringFields,
                                       @Nonnull final List<DbFieldTyped<T, ?>> typedFields,
                                       @Nonnull final List<DbFieldNested<T, ?>> nestedFields,
                                       @Nonnull final List<DbFieldRelation<T, ?>> relationFields) {
    final var saveables = new ArrayList<T>();
    final var errors = new ArrayList<CsvMappingRowError>();

    final var stringFieldByKey = indexStringFields(stringFields);
    final var typedFieldByKey = indexTypedFields(typedFields);
    final var relationFieldByKey = indexRelationFields(relationFields);

    final var requiredKeys = new HashSet<String>();
    stringFields.stream().filter(DbField::required).forEach(f -> requiredKeys.add(f.key()));
    typedFields.stream().filter(DbFieldTyped::required).forEach(f -> requiredKeys.add(f.key()));
    relationFields.stream().filter(DbFieldRelation::required).forEach(f -> requiredKeys.add(f.key()));

    for (int i = 0; i < csvRows.size(); i++) {
      final int rowIndex = i + 1;
      final var row = normalizeHashMap(csvRows.get(i).row());

      final var target = newTarget();
      final var setKeys = new HashSet<String>();

      for (final var entry : mapping.entrySet()) {
        final var csvColumn = StringSanitizer.cleanNullSave(entry.getKey());
        final var dbKey = entry.getValue();

        final var raw = row.get(csvColumn);
        final var value = normalizeString(raw);
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
            continue;
          }

          final var rf = relationFieldByKey.get(dbKey);
          if (rf != null) {
            applyRelation(rf, target, value);
            setKeys.add(dbKey);
          }
        } catch (Exception ex) {
          errors.add(new CsvMappingRowError(rowIndex, injector.getInstance(TranslationHelper.class).getTranslation("server.fehler.bei.feld.0.1", dbKey, ex.getMessage())));
        }
      }

      for (final var nestedField : nestedFields) {
        try {
          processAndApplyNestedField(nestedField, target, mapping, row, rowIndex, errors);
        } catch (Exception ex) {
          errors.add(new CsvMappingRowError(rowIndex, injector.getInstance(TranslationHelper.class).getTranslation("server.fehler.bei.verschachteltem.feld.0.1", nestedField.prefix(), ex.getMessage())));
        }
      }

      final var missing = requiredKeys.stream()
              .filter(k -> !setKeys.contains(k))
              .toList();

      if (!missing.isEmpty()) {
        errors.add(new CsvMappingRowError(rowIndex, injector.getInstance(TranslationHelper.class).getTranslation("server.pflichtfelder.fehlen.0", String.join(", ", missing))));
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
    final var prefix = nestedField.prefix() + Publ.DOT;
    final var nestedEntity = nestedField.nestedFactory().get();

    final var stringFieldByKey = indexStringFields(nestedField.nestedStringFields());
    final var typedFieldByKey = indexTypedFields(nestedField.nestedTypedFields());

    final var setKeys = new HashSet<String>();
    boolean hasAnyValue = false;

    for (final var entry : mapping.entrySet()) {
      final var csvColumn = StringSanitizer.cleanNullSave(entry.getKey());
      final var dbKey = entry.getValue();

      if (!dbKey.startsWith(prefix)) {
        continue;
      }

      final var nestedKey = dbKey.substring(prefix.length());
      final var raw = row.get(csvColumn);
      final var value = normalizeString(raw);

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
        errors.add(new CsvMappingRowError(rowIndex, injector.getInstance(TranslationHelper.class).getTranslation("server.fehler.bei.feld.0.1", dbKey, ex.getMessage())));
      }
    }

    if (!hasAnyValue) {
      return null;
    }

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
        errors.add(new CsvMappingRowError(rowIndex, injector.getInstance(TranslationHelper.class).getTranslation("server.pflichtfelder.fur.0.fehlen.1", nestedField.prefix(), String.join(", ", missing))));
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

  @SuppressWarnings("unchecked")
  private static <T> void applyRelation(@Nonnull final DbFieldRelation<T, ?> field,
                                        @Nonnull final T target,
                                        @Nonnull final String raw) {
    final var resolved = ((DbFieldRelation<T, Object>) field).resolver().apply(raw);
    ((DbFieldRelation<T, Object>) field).setter().accept(target, resolved);
  }

  @Nonnull
  private static <T> Map<String, DbField<T>> indexStringFields(@Nonnull final List<DbField<T>> fields) {
    final var map = new HashMap<String, DbField<T>>();
    for (final var field : fields) {
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

  @Nonnull
  private static <T> Map<String, DbFieldRelation<T, ?>> indexRelationFields(@Nonnull final List<DbFieldRelation<T, ?>> fields) {
    final var map = new HashMap<String, DbFieldRelation<T, ?>>();
    for (final var field : fields) {
      map.put(field.key(), field);
    }

    return map;
  }

  @Nullable
  protected final String normalizeString(@Nullable final String s) {
    return StringSanitizer.clean(s);
  }

  @Nonnull
  private LinkedHashMap<String, String> normalizeHashMap(@Nonnull final Map<String, String> rawMap) {
    final var cleanMap = new LinkedHashMap<String, String>();

    for (final var rowEntry : rawMap.entrySet()) {
      cleanMap.put(
              StringSanitizer.cleanNullSave(rowEntry.getKey()),
              rowEntry.getValue()
      );
    }

    return cleanMap;
  }
}