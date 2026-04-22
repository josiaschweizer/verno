package ch.verno.ui.base.components.mapping;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public record FieldOption<T>(@Nonnull String key,
                             @Nonnull String label,
                             @Nullable T value) {

  @Nonnull
  public static <T> FieldOption<T> ignore(@Nonnull final String label) {
    return new FieldOption<>(VABaseColumnMappingPanel.IGNORE_KEY, label, null);
  }

  @Nonnull
  public static <T> FieldOption<T> of(@Nonnull final String key,
                                      @Nonnull final String label,
                                      @Nonnull final T value) {
    return new FieldOption<>(key, label, value);
  }
}