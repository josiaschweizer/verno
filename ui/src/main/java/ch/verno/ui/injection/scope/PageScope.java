package ch.verno.ui.injection.scope;

import com.google.inject.Key;
import com.google.inject.OutOfScopeException;
import com.google.inject.Provider;
import com.google.inject.Scope;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class PageScope implements Scope {

  private static final String DATA_KEY = PageScope.class.getName() + ".storage";

  @Override
  public <T> Provider<T> scope(final Key<T> key, final Provider<T> unscoped) {
    return () -> {
      final UI ui = UI.getCurrent();
      if (ui == null) {
        throw new OutOfScopeException("No UI bound to current thread for @PageScoped " + key);
      }

      @SuppressWarnings("unchecked")
      Map<Key<?>, Object> storage = (Map<Key<?>, Object>) ComponentUtil.getData(ui, DATA_KEY);
      if (storage == null) {
        storage = new ConcurrentHashMap<>();
        ComponentUtil.setData(ui, DATA_KEY, storage);
      }

      @SuppressWarnings("unchecked")
      T instance = (T) storage.get(key);
      if (instance == null) {
        instance = unscoped.get();
        storage.put(key, instance);
      }
      return instance;
    };
  }

  public static void resetCurrentPage() {
    Optional.ofNullable(UI.getCurrent()).ifPresent(ui -> ComponentUtil.setData(ui, DATA_KEY, new ConcurrentHashMap<Key<?>, Object>()));
  }
}