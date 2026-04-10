package ch.verno.ui.base.components.button;

import ch.verno.publ.CssImportConstants;
import ch.verno.publ.Publ;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.shared.Registration;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

@CssImport(CssImportConstants.VA_BUTTON)
public class VAButton extends Button {

  private static final String JS_CLICK_PARENT = "this.click()";
  private static final String READONLY_CLASS_NAME = "va-button-readonly";

  private boolean pseudoEnabled = true;
  private boolean forwardClickToParent = false;
  @Nullable private String disableTooltipText;

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
                  @Nonnull final Component component) {
    super(text, component);
  }

  public VAButton(@Nonnull final String text,
                  @Nonnull final ComponentEventListener<ClickEvent<Button>> listener) {
    super(text);
    addClickListener(listener);
  }

  public VAButton(@Nonnull final Component icon,
                  @Nonnull final ComponentEventListener<ClickEvent<Button>> listener) {
    super(icon);
    addClickListener(listener);
  }

  public VAButton(@Nonnull final String text,
                  @Nonnull final Component icon,
                  @Nonnull final ComponentEventListener<ClickEvent<Button>> listener) {
    super(text, icon);
    addClickListener(listener);
  }

  public VAButton(@Nonnull final Component icon,
                  @Nonnull final String text,
                  @Nonnull final ComponentEventListener<ClickEvent<Button>> listener) {
    super(text, icon);
    addClickListener(listener);
  }

  public void removePseudoEnabled() {
    pseudoEnabled = true;
    forwardClickToParent = false;
    disableTooltipText = null;

    removeClassName(READONLY_CLASS_NAME);
    setTooltipText(Publ.EMPTY_STRING);
  }

  public void setPseudoEnabled(final boolean enabled,
                               @Nonnull final String tooltipText) {
    setPseudoEnabled(enabled, tooltipText, true);
  }

  public void setPseudoEnabled(final boolean enabled,
                               @Nonnull final String tooltipText,
                               final boolean forwardClickToParent) {
    this.pseudoEnabled = enabled;
    this.forwardClickToParent = forwardClickToParent;
    this.disableTooltipText = enabled ? null : tooltipText;

    if (enabled) {
      removeClassName(READONLY_CLASS_NAME);
      setTooltipText(Publ.EMPTY_STRING);
    } else {
      addClassName(READONLY_CLASS_NAME);
      setTooltipText(tooltipText);
    }
  }

  @Override
  public Registration addClickListener(@Nonnull final ComponentEventListener<ClickEvent<Button>> listener) {
    return addClickListener(listener, false);
  }

  @Nonnull
  public Registration addClickListener(@Nonnull final ComponentEventListener<ClickEvent<Button>> listener,
                                       final boolean ignorePseudoDisabled) {
    return super.addClickListener(event -> {
      if (!pseudoEnabled && !ignorePseudoDisabled) {
        if (forwardClickToParent) {
          getParent().ifPresent(parent -> parent.getElement().executeJs(JS_CLICK_PARENT));
        }
        return;
      }

      listener.onComponentEvent(event);
    });
  }

  public boolean isPseudoEnabled() {
    return pseudoEnabled;
  }

  @Nullable
  public String getDisableTooltipText() {
    return disableTooltipText;
  }
}