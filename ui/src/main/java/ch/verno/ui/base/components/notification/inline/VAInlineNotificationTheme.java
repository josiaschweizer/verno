package ch.verno.ui.base.components.notification.inline;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;

import javax.annotation.Nonnull;

public enum VAInlineNotificationTheme {
  SUCCESS("notification-success", VaadinIcon.CHECK_CIRCLE),
  ERROR("notification-error", VaadinIcon.CLOSE_CIRCLE),
  WARNING("notification-warning", VaadinIcon.WARNING),
  INFO("notification-info", VaadinIcon.INFO_CIRCLE);

  @Nonnull private final String className;
  @Nonnull private final VaadinIcon icon;

  VAInlineNotificationTheme(@Nonnull final String className,
                            @Nonnull final VaadinIcon icon) {
    this.className = className;
    this.icon = icon;
  }

  @Nonnull
  public String getClassName() {
    return className;
  }

  @Nonnull
  public Component createIcon() {
    final Icon icon = this.icon.create();
    icon.addClassName(VAInlineNotification.ICON_CLASSNAME);
    return icon;
  }

}