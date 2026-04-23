package ch.verno.ui.base.components.contextmenu;

import ch.verno.ui.base.factory.SpanFactory;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.icon.VaadinIcon;
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
  public static ActionDef create(@Nonnull final String text,
                                 @Nonnull final VaadinIcon icon,
                                 @Nonnull final Runnable runnable) {
    return new ActionDef(SpanFactory.createSpan(text, icon), runnable, true);
  }

  @Nonnull
  public static ActionDef create(@Nonnull final String text,
                                 @Nonnull final VaadinIcon icon,
                                 @Nonnull final Runnable runnable,
                                 final boolean enabled) {
    return new ActionDef(SpanFactory.createSpan(text, icon), runnable, enabled);
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