package ch.verno.ui.base.components.notification;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;

@CssImport("./components/notification/va-notification.css")
public class NotificationFactory {

  public enum NotificationType {
    SUCCESS, ERROR, WARNING, INFO
  }

  public static void showSuccessNotification(String message) {
    show(message, NotificationType.SUCCESS);
  }

  public static void showErrorNotification(String message) {
    show(message, NotificationType.ERROR);
  }

  public static void showWarningNotification(String message) {
    show(message, NotificationType.WARNING);
  }

  public static void showInfoNotification(String message) {
    show(message, NotificationType.INFO);
  }

  public static void show(String message, NotificationType type) {
    show(message, null, type, 4000);
  }

  public static void show(String message, String description, NotificationType type) {
    show(message, description, type, 4000);
  }

  public static void show(String message, String description, NotificationType type, int duration) {
    Notification notification = new Notification();
    notification.setDuration(duration);
    notification.setPosition(Notification.Position.BOTTOM_END);

    Div layout = new Div();
    layout.addClassName("modern-notification");
    layout.addClassName("notification-" + type.name().toLowerCase());

    Icon icon = getIconForType(type);
    icon.addClassName("notification-icon");

    Div content = new Div();
    content.addClassName("notification-content");

    Span messageSpan = new Span(message);
    messageSpan.addClassName("notification-message");
    content.add(messageSpan);

    if (description != null && !description.isEmpty()) {
      Span descriptionSpan = new Span(description);
      descriptionSpan.addClassName("notification-description");
      content.add(descriptionSpan);
    }

    Icon closeIcon = VaadinIcon.CLOSE_SMALL.create();
    closeIcon.addClassName("notification-close");
    closeIcon.getElement().setAttribute("aria-label", "Close");
    closeIcon.addClickListener(e -> notification.close());

    layout.add(icon, content, closeIcon);
    notification.add(layout);
    notification.open();
  }

  private static Icon getIconForType(NotificationType type) {
    return switch (type) {
      case SUCCESS -> VaadinIcon.CHECK_CIRCLE.create();
      case ERROR -> VaadinIcon.CLOSE_CIRCLE.create();
      case WARNING -> VaadinIcon.WARNING.create();
      case INFO -> VaadinIcon.INFO_CIRCLE.create();
    };
  }
}