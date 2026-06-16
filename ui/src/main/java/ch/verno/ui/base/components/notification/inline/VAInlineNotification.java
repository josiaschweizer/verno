package ch.verno.ui.base.components.notification.inline;

import ch.verno.publ.CssImportConstants;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import jakarta.annotation.Nonnull;
import org.jetbrains.annotations.NonNls;

@CssImport(CssImportConstants.VA_INLINE_NOTIFICATION)
public class VAInlineNotification extends Composite<VerticalLayout> {

  @NonNls public static final String VA_INLINE_NOTIFICATION_CLASSNAME = "va-inline-notification";
  @NonNls public static final String HEADER_CLASSNAME = "va-inline-notification-header";
  @NonNls public static final String ICON_CLASSNAME = "va-inline-notification-icon";
  @NonNls public static final String CONTENT_CLASSNAME = "va-inline-notification-content";
  @NonNls public static final String TITLE_CLASSNAME = "va-inline-notification-title";
  @NonNls public static final String DESCRIPTION_CLASSNAME = "va-inline-notification-description";
  @NonNls public static final String ACTIONS_CLASSNAME = "va-inline-notification-actions";

  @NonNls private static final String ACTIONS_BELOW_CONTENT_CLASSNAME = "actions-below-content";
  @NonNls private static final String ACTIONS_RIGHT_CENTERED_CLASSNAME = "actions-right-centered";

  @Nonnull private final HorizontalLayout headerLayout;

  @Nonnull private final Div iconSlot;
  @Nonnull private final VerticalLayout contentSlot;
  @Nonnull private final Div titleSlot;
  @Nonnull private final Div descriptionSlot;
  @Nonnull private final HorizontalLayout actionSlot;

  @Nonnull private VAInlineNotificationTheme theme;
  @Nonnull private VAInlineNotificationActionAlignment actionAlignment = VAInlineNotificationActionAlignment.BELOW_CONTENT;

  public VAInlineNotification() {
    this(VAInlineNotificationTheme.INFO);
  }

  public VAInlineNotification(@Nonnull final VAInlineNotificationTheme theme) {
    this.headerLayout = new HorizontalLayout();

    this.iconSlot = new Div();
    this.contentSlot = new VerticalLayout();
    this.titleSlot = new Div();
    this.descriptionSlot = new Div();
    this.actionSlot = new HorizontalLayout();

    this.theme = theme;

    initSlots();
    setTheme(theme);
  }

  private void initSlots() {
    getContent().addClassName(VA_INLINE_NOTIFICATION_CLASSNAME);

    headerLayout.addClassName(HEADER_CLASSNAME);
    iconSlot.addClassName(ICON_CLASSNAME);
    contentSlot.addClassName(CONTENT_CLASSNAME);
    actionSlot.addClassName(ACTIONS_CLASSNAME);

    contentSlot.add(titleSlot, descriptionSlot);
    headerLayout.add(iconSlot, contentSlot, actionSlot);

    getContent().add(headerLayout);

    descriptionSlot.setVisible(false);
    actionSlot.setVisible(false);

    setActionAlignment(actionAlignment);
  }

  public void setActionAlignment(@Nonnull final VAInlineNotificationActionAlignment actionAlignment) {
    this.actionAlignment = actionAlignment;

    getContent().removeClassNames(
            ACTIONS_BELOW_CONTENT_CLASSNAME,
            ACTIONS_RIGHT_CENTERED_CLASSNAME
    );
    getContent().addClassName(actionAlignment.getClassName());
  }

  public void setTheme(@Nonnull final VAInlineNotificationTheme theme) {
    getContent().removeClassName(this.theme.getClassName());

    this.theme = theme;

    getContent().addClassName(theme.getClassName());
    setIcon(theme.createIcon());
  }

  public void setIcon(@Nonnull final Component component) {
    iconSlot.removeAll();
    iconSlot.add(component);
  }

  public void setTitle(@Nonnull final String text) {
    titleSlot.removeAll();

    final Span span = new Span(text);
    span.addClassName(TITLE_CLASSNAME);

    titleSlot.add(span);
  }

  public void setDescription(@Nonnull final String text) {
    descriptionSlot.removeAll();

    final Span span = new Span(text);
    span.addClassName(DESCRIPTION_CLASSNAME);

    descriptionSlot.add(span);
    descriptionSlot.setVisible(true);
  }

  public void clearDescription() {
    descriptionSlot.removeAll();
    descriptionSlot.setVisible(false);
  }

  public void setActions(@Nonnull final Component... components) {
    actionSlot.removeAll();
    actionSlot.add(components);
    actionSlot.setVisible(components.length > 0);
  }

  public void clearActions() {
    actionSlot.removeAll();
    actionSlot.setVisible(false);
  }


  public enum VAInlineNotificationActionAlignment {
    BELOW_CONTENT(ACTIONS_BELOW_CONTENT_CLASSNAME),
    RIGHT_CENTERED(ACTIONS_RIGHT_CENTERED_CLASSNAME);

    @Nonnull private final String className;

    VAInlineNotificationActionAlignment(@Nonnull final String className) {
      this.className = className;
    }

    @Nonnull
    public String getClassName() {
      return className;
    }
  }
}