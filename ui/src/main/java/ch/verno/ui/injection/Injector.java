package ch.verno.ui.injection;

import com.vaadin.flow.i18n.I18NProvider;
import jakarta.annotation.Nonnull;

public final class Injector {

  @Nonnull private final com.google.inject.Injector delegate;

  public Injector(@Nonnull final com.google.inject.Injector delegate) {
    this.delegate = delegate;
  }

  @Nonnull
  public <T> T getInstance(@Nonnull final Class<T> type) {
    return delegate.getInstance(type);
  }

  public void injectMembers(@Nonnull final Object instance) {
    delegate.injectMembers(instance);
  }

  @Nonnull
  public I18NProvider getI18NProvider() {
    return delegate.getInstance(I18NProvider.class);
  }
}