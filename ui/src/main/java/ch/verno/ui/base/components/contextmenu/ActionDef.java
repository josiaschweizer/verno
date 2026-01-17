package ch.verno.ui.base.components.contextmenu;

import com.vaadin.flow.component.Component;
import jakarta.annotation.Nonnull;

public final class ActionDef {

  @Nonnull private final Component component;
  @Nonnull private final Runnable runnable;
  private final boolean enabled;

  private ActionDef(@Nonnull final Component component,
                    @Nonnull final Runnable runnable,
                    final boolean enabled) {
    this.component = component;
    this.runnable = runnable;
    this.enabled = enabled;
  }

  @Nonnull
  public static ActionDef create(@Nonnull final Component component,
                                 @Nonnull final Runnable runnable) {
    return new ActionDef(component, runnable, true);
  }

  @Nonnull
  public static ActionDef create(@Nonnull final Component component,
                                 @Nonnull final Runnable runnable,
                                 final boolean enabled) {
    return new ActionDef(component, runnable, enabled);
  }

  @Nonnull
  public Component getComponent() {
    return component;
  }

  @Nonnull
  public Runnable getRunnable() {
    return runnable;
  }

  public boolean isEnabled() {
    return enabled;
  }
}