package ch.verno.lib;

import jakarta.annotation.Nonnull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class New {

  public static <T> ArrayList<T> arrayList() {
    return new ArrayList<>();
  }

  @Nonnull
  public static <T> ArrayList<T> arrayList(@Nonnull T item) {
    final var list = new ArrayList<T>();
    list.add(item);
    return list;
  }

  @Nonnull
  @SafeVarargs
  public static <T> ArrayList<T> arrayList(@Nonnull T... items) {
    final var list = new ArrayList<T>(items.length);
    Collections.addAll(list, items);
    return list;
  }

  @SafeVarargs
  public static <T> ArrayList<T> arrayList(@Nonnull List<T>... lists) {
    final var list = new ArrayList<T>();
    for (final var l : lists) {
      list.addAll(l);
    }
    return list;
  }

  @Nonnull
  public static <K, V> HashMap<K, V> hashMap() {
    return new HashMap<>();
  }

  @Nonnull
  public static <K, V> HashMap<K, V> hashMap(@Nonnull final K key, @Nonnull final V value) {
    final var map = new HashMap<K, V>();
    map.put(key, value);
    return map;
  }
}