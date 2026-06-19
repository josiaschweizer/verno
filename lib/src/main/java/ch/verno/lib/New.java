package ch.verno.lib;

import jakarta.annotation.Nonnull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class New {

  public static final int BASE_LIST_SIZE = 2;
  public static final int BASE_MAP_SIZE = 2;

  @Nonnull
  public static <T> List<T> list() {
    return arrayList();
  }

  @Nonnull
  @SafeVarargs
  public static <T> List<T> list(@Nonnull final T... items) {
    return arrayList(items);
  }

  @Nonnull
  public static <T> List<T> copyList(@Nonnull final List<T> lists) {
    return combinedArrayList(lists);
  }

  @Nonnull
  public static <T> ArrayList<T> arrayList() {
    return new ArrayList<>(BASE_LIST_SIZE);
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
    return combinedArrayList(lists);
  }

  @Nonnull
  @SafeVarargs
  protected static <T> ArrayList<T> combinedArrayList(@Nonnull final List<? extends T>... lists) {
    final var result = new ArrayList<T>(BASE_LIST_SIZE);
    for (final var list : lists) {
      result.addAll(list);
    }

    return result;
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
    return new HashMap<>(BASE_MAP_SIZE);
  }

  @Nonnull
  public static <K, V> HashMap<K, V> hashMap(@Nonnull final K key, @Nonnull final V value) {
    final var map = new HashMap<K, V>(BASE_MAP_SIZE);
    map.put(key, value);
    return map;
  }

  @Nonnull
  public static <K, V> ConcurrentHashMap<K, V> concurrentHashMap() {
    return new ConcurrentHashMap<K, V>(BASE_MAP_SIZE);
  }
}