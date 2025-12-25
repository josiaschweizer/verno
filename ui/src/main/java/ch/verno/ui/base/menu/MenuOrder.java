package ch.verno.ui.base.menu;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class MenuOrder implements Comparable<MenuOrder> {

  @Nonnull
  private final List<Integer> parts;

  private MenuOrder(@Nonnull final List<Integer> parts) {
    this.parts = List.copyOf(parts);
  }

  @Nonnull
  public static MenuOrder of(final double order) {
    final var bd = BigDecimal.valueOf(order).stripTrailingZeros();
    final var s = bd.toPlainString();

    final String[] split = s.split("\\.");
    final var parts = new ArrayList<Integer>();

    parts.add(Integer.parseInt(split[0]));

    if (split.length > 1 && !split[1].isBlank()) {
      for (char c : split[1].toCharArray()) {
        if (Character.isDigit(c)) {
          parts.add(Character.digit(c, 10));
        }
      }
    }

    return new MenuOrder(parts);
  }

  public int depth() {
    return parts.size();
  }

  public boolean isRoot() {
    return depth() == 1;
  }

  @Nonnull
  public MenuOrder parent() {
    if (isRoot()) {
      return this;
    }

    return new MenuOrder(parts.subList(0, parts.size() - 1));
  }

  @Nonnull
  public List<Integer> parts() {
    return parts;
  }

  @Override
  public int compareTo(@Nonnull final MenuOrder other) {
    final int max = Math.max(this.parts.size(), other.parts.size());

    for (int i = 0; i < max; i++) {
      final var a = i < this.parts.size() ? this.parts.get(i) : null;
      final var b = i < other.parts.size() ? other.parts.get(i) : null;

      if (a == null) {
        return -1;
      }
      if (b == null) {
        return 1;
      }

      final var compare = Integer.compare(a, b);
      if (compare != 0) {
        return compare;
      }
    }

    return 0;
  }

  @Override
  public boolean equals(@Nullable final Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof MenuOrder that)) {
      return false;
    }

    return parts.equals(that.parts);
  }

  @Override
  public int hashCode() {
    return Objects.hash(parts);
  }

  @Nonnull
  @Override
  public String toString() {
    return parts.toString();
  }
}