package ch.verno.ui.injection.scope;

import com.google.inject.Key;
import com.google.inject.OutOfScopeException;
import com.google.inject.Provider;
import com.google.inject.Scope;
import com.vaadin.flow.server.VaadinSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionScope implements Scope {

  private static final String ATTRIBUTE_KEY = SessionScope.class.getName() + ".storage";

  @Override
  public <T> Provider<T> scope(final Key<T> key, final Provider<T> unscoped) {
    return () -> {
      final VaadinSession session = VaadinSession.getCurrent();
      if (session == null) {
        throw new OutOfScopeException("No VaadinSession bound to current thread for @SessionScoped " + key);
      }

      session.lock();
      try {
        @SuppressWarnings("unchecked")
        Map<Key<?>, Object> storage = (Map<Key<?>, Object>) session.getAttribute(ATTRIBUTE_KEY);
        if (storage == null) {
          storage = new ConcurrentHashMap<>();
          session.setAttribute(ATTRIBUTE_KEY, storage);
        }

        @SuppressWarnings("unchecked")
        T instance = (T) storage.get(key);
        if (instance == null) {
          instance = unscoped.get();
          storage.put(key, instance);
        }
        return instance;
      } finally {
        session.unlock();
      }
    };
  }
}