package ch.verno.lib;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class New {

  public static <T> ArrayList<T> arrayList() {
    return new ArrayList<>();
  }

  public static <T> ArrayList<T> arrayList(T item) {
    final var list = new ArrayList<T>();
    list.add(item);
    return list;
  }

  @SafeVarargs
  public static <T> ArrayList<T> arrayList(T... items) {
    final var list = new ArrayList<T>(items.length);
    Collections.addAll(list, items);
    return list;
  }

}
