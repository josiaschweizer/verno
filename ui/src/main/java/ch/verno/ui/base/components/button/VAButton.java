package ch.verno.ui.base.components.button;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import jakarta.annotation.Nonnull;

public class VAButton extends Button {

  public VAButton() {
    super();
  }

  public VAButton(@Nonnull final String text) {
    super(text);
  }

  public VAButton(@Nonnull final Component component) {
    super(component);
  }

  public VAButton(@Nonnull final String text,
                  @Nonnull final ComponentEventListener<ClickEvent<Button>> listener) {
    super(text, listener);
  }

  public VAButton(@Nonnull final Component icon,
                  @Nonnull final ComponentEventListener<ClickEvent<Button>> listener) {
    super(icon, listener);
  }

  public VAButton(@Nonnull final String text,
                  @Nonnull final Component icon,
                  @Nonnull final ComponentEventListener<ClickEvent<Button>> listener) {
    super(text, icon, listener);
  }

  public VAButton(@Nonnull final Component icon,
                  @Nonnull final String text,
                  @Nonnull final ComponentEventListener<ClickEvent<Button>> listener) {
    super(text, icon, listener);
  }
}
