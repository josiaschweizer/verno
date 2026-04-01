package ch.verno.ui.base.components.has.sessionstorage;

import com.vaadin.flow.dom.Element;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public interface HasSessionStorage {

  @Nonnull
  Element getStorageElement();

  default void saveToSessionStorage(@Nonnull final String key,
                                    @Nonnull final String value) {
    getStorageElement().executeJs(
            "sessionStorage.setItem($0, $1);",
            buildStorageKey(key),
            value
    );
  }

  default void loadFromSessionStorage(@Nonnull final String key,
                                      @Nonnull final SessionStorageValueConsumer consumer) {
    getStorageElement().executeJs(
            "return sessionStorage.getItem($0);",
            buildStorageKey(key)
    ).then(String.class, consumer::accept);
  }

  default void removeFromSessionStorage(@Nonnull final String key) {
    getStorageElement().executeJs(
            "sessionStorage.removeItem($0);",
            buildStorageKey(key)
    );
  }

  @Nonnull
  default String buildStorageKey(@Nonnull final String key) {
    return getClass().getName() + ":" + key;
  }

  @FunctionalInterface
  interface SessionStorageValueConsumer {
    void accept(@Nullable String value);
  }
}