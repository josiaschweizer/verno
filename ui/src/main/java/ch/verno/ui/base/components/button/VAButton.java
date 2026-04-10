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

  private boolean pseudoReadonly = false;
  @Nullable private String readOnlyTooltip;

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

  public void setPseudoReadOnly(final boolean readonly,
                                @Nonnull final String tooltipText) {
    this.pseudoReadonly = readonly;
    this.readOnlyTooltip = tooltipText;

    if (readonly) {
      addClassName("va-button-readonly");
      setTooltipText(tooltipText);
    } else {
      removeClassName("va-button-readonly");
      setTooltipText(Publ.EMPTY_STRING);
    }
  }

  @Override
  public Registration addClickListener(@Nonnull final ComponentEventListener<ClickEvent<Button>> listener) {
    return addClickListener(listener, false);
  }

  public Registration addClickListener(@Nonnull final ComponentEventListener<ClickEvent<Button>> listener,
                                       final boolean ignorePseudoReadOnly) {
    return super.addClickListener(event -> {
      if (pseudoReadonly && !ignorePseudoReadOnly) {
        return;
      }

      listener.onComponentEvent(event);
    });
  }

  public boolean isPseudoReadonly() {
    return pseudoReadonly;
  }

  @Nullable
  public String getReadOnlyTooltip() {
    return readOnlyTooltip;
  }
}