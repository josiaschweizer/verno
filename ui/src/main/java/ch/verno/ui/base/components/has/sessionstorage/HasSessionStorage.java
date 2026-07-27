package ch.verno.ui.base.components.has.sessionstorage;

import ch.verno.lib.Publ;
import com.vaadin.flow.dom.Element;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.jetbrains.annotations.NonNls;

public interface HasSessionStorage {

  @NonNls String SET_SESSION_STORAGE_ITEM_JS = "sessionStorage.setItem($0, $1);";
  @NonNls String GET_SESSION_STORAGE_ITEM_JS = "return sessionStorage.getItem($0);";
  @NonNls String REMOVE_SESSION_STORAGE_ITEM_JS = "sessionStorage.removeItem($0);";

  @Nonnull
  Element getStorageElement();

  default void saveToSessionStorage(@Nonnull final String key,
                                    @Nonnull final String value) {
    getStorageElement().executeJs(SET_SESSION_STORAGE_ITEM_JS, buildStorageKey(key), value);
  }

  default void loadFromSessionStorage(@Nonnull final String key,
                                      @Nonnull final SessionStorageValueConsumer consumer) {
    getStorageElement().executeJs(GET_SESSION_STORAGE_ITEM_JS, buildStorageKey(key)).then(String.class, consumer::accept);
  }

  default void removeFromSessionStorage(@Nonnull final String key) {
    getStorageElement().executeJs(REMOVE_SESSION_STORAGE_ITEM_JS, buildStorageKey(key));
  }

  @Nonnull
  default String buildStorageKey(@Nonnull final String key) {
    return getClass().getName() + Publ.COLON + key;
  }

  @FunctionalInterface
  interface SessionStorageValueConsumer {

    void accept(@Nullable String value);

  }
}