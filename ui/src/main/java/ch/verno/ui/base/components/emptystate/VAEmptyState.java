package ch.verno.ui.base.components.emptystate;

import ch.verno.lib.CssImportConstants;
import ch.verno.ui.base.components.button.VAButton;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.jetbrains.annotations.NonNls;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@CssImport(CssImportConstants.VA_EMPTY_STATE)
public class VAEmptyState extends Composite<VerticalLayout> implements HasTheme {

  @NonNls public static final String THEME_BORDERLESS = "borderless";
  @NonNls public static final String VA_EMPTY_STATE_CLASSNAME = "va-empty-state";
  @NonNls public static final String ICON_CLASSNAME = "va-empty-state-icon";
  @NonNls public static final String TITLE_CLASSNAME = "va-empty-state-title";
  @NonNls public static final String DESCRIPTION_CLASSNAME = "va-empty-state-description";
  @NonNls public static final String ACTIONS_CLASSNAME = "va-empty-state-actions";

  @Nonnull private final Div iconSlot;
  @Nonnull private final Div titleSlot;
  @Nonnull private final Div descriptionSlot;
  @Nonnull private final Div actionSlot;

  public VAEmptyState() {
    this.iconSlot = new Div();
    this.titleSlot = new Div();
    this.descriptionSlot = new Div();
    this.actionSlot = new Div();

    initLayout();
  }

  public void setIcon(@Nonnull VaadinIcon iconId) {
    setIcon(iconId.create());
  }

  public void setIcon(@Nullable Icon icon) {
    setIcon(new Span(icon));
  }

  public void setIcon(@Nonnull Component icon) {
    setSlotContent(iconSlot, icon);
  }

  public void setTitle(@Nonnull String title) {
    setTitle(new Span(title));
  }

  public void setTitle(@Nonnull Component title) {
    setSlotContent(titleSlot, title);
  }

  public void setDescription(@Nonnull String description) {
    setDescription(new Span(description));
  }

  public void setDescription(@Nonnull Component description) {
    setSlotContent(descriptionSlot, description);
  }

  public void removeDescriptions() {
    descriptionSlot.removeAll();
  }

  public void setAction(@Nonnull Component action) {
    setSlotContent(actionSlot, action);
  }

  public void setAction(@Nonnull String title,
                        @Nonnull ComponentEventListener<ClickEvent<Button>> listener) {
    setAction(title, null, listener);
  }

  public void setAction(@Nonnull String title,
                        @Nullable VaadinIcon icon,
                        @Nonnull ComponentEventListener<ClickEvent<Button>> listener) {
    final var button = new VAButton(title);
    if (icon != null) {
      button.setIcon(icon.create());
    }

    button.addClickListener(listener);

    setAction(button);
  }

  public void addToActionSlot(@Nonnull VAButton... components) {
    actionSlot.add(components);
  }

  public void setBorderless(boolean borderless) {
    getThemeNames().set(THEME_BORDERLESS, borderless);
  }

  public boolean isBorderless() {
    return getThemeNames().contains(THEME_BORDERLESS);
  }

  public void setHeightFull() {
    getContent().setHeightFull();
  }

  private void initLayout() {
    final var content = getContent();

    content.addClassName(VA_EMPTY_STATE_CLASSNAME);
    content.setPadding(false);
    content.setSpacing(false);
    content.setAlignItems(VerticalLayout.Alignment.CENTER);
    content.setJustifyContentMode(VerticalLayout.JustifyContentMode.CENTER);

    iconSlot.addClassName(ICON_CLASSNAME);
    titleSlot.addClassName(TITLE_CLASSNAME);
    descriptionSlot.addClassName(DESCRIPTION_CLASSNAME);
    actionSlot.addClassName(ACTIONS_CLASSNAME);

    content.add(iconSlot, titleSlot, descriptionSlot, actionSlot);
  }

  private void setSlotContent(@Nonnull Div slot,
                              @Nullable Component component) {
    slot.removeAll();

    if (component != null) {
      slot.add(component);
    }
  }
}