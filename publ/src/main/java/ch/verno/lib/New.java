package ch.verno.lib;

import jakarta.annotation.Nonnull;

import java.util.*;

public class New {

  @Nonnull
  public static <T> List<T> list() {
    return arrayList();
  }

  @Nonnull
  public static <T> List<T> list(@Nonnull final T item) {
    return arrayList(item);
  }

  @Nonnull
  @SafeVarargs
  public static <T> List<T> list(@Nonnull final T... items) {
    return arrayList(items);
  }

  @Nonnull
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
  public static <K, V> Map<K, V> map() {
    return hashMap();
  }

  @Nonnull
  public static <K, V> Map<K, V> map(@Nonnull final K key, @Nonnull final V value) {
    return hashMap(key, value);
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