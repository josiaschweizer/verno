package ch.verno.ui.base.components.toolbar;

import ch.verno.ui.base.components.badge.UserActionBadge;
import ch.verno.ui.lib.Routes;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.Nonnull;
import org.jspecify.annotations.Nullable;

public final class ViewToolbar extends Composite<HorizontalLayout> {

  @Nonnull private final HorizontalLayout actions;
  @Nonnull private final HorizontalLayout userAction;

  public ViewToolbar(@Nullable final String viewTitle,
                     @Nonnull final Component... initialActions) {
    final var layout = getContent();
    layout.setPadding(true);
    layout.setWrap(true);
    layout.setWidthFull();
    layout.addClassName(LumoUtility.Border.BOTTOM);
    layout.addClassName(LumoUtility.Margin.Top.MEDIUM);
    layout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

    final var drawerToggle = new DrawerToggle();
    drawerToggle.addClassNames(LumoUtility.Margin.NONE);

    final var title = new H1(viewTitle == null ? "" : viewTitle);
    title.addClassNames(
            LumoUtility.FontSize.XLARGE,
            LumoUtility.Margin.NONE,
            LumoUtility.FontWeight.LIGHT
    );

    final var toggleAndTitle = new HorizontalLayout(drawerToggle, title);
    toggleAndTitle.setPadding(false);
    toggleAndTitle.setSpacing(true);
    toggleAndTitle.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

    actions = new HorizontalLayout();
    actions.setPadding(false);
    actions.setSpacing(true);
    actions.setWrap(true);
    actions.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

    userAction = new HorizontalLayout();
    userAction.setPadding(false);
    userAction.setSpacing(false);
    userAction.setWrap(true);
    userAction.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

    layout.add(toggleAndTitle, actions, userAction);
    layout.setFlexGrow(1, toggleAndTitle);

    if (initialActions.length > 0) {
      addActions(initialActions);
    }
  }

  @Nonnull
  public ViewToolbar addAction(@Nonnull final Component component) {
    actions.add(component);
    return this;
  }

  @Nonnull
  public ViewToolbar addActions(@Nonnull final Component... components) {
    actions.add(components);
    return this;
  }

  @Nonnull
  public ViewToolbar addActionButton(@Nonnull final Component button,
                                     final boolean asFirst) {
    if (asFirst) {
      actions.addComponentAsFirst(button);
    } else {
      actions.add(button);
    }
    return this;
  }

  @Nonnull
  public ViewToolbar addUserAction(@Nonnull final Component component) {
    userAction.add(component);
    return this;
  }

  public static Component group(@Nonnull final Component... components) {
    final var group = new HorizontalLayout(components);
    group.setWrap(true);
    return group;
  }
}