package ch.verno.ui.base.components.notification;

import ch.verno.lib.CssImportConstants;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

@CssImport(CssImportConstants.VA_NOTIFICATION)
public class VANotification extends Notification {

  public enum NotificationType {
    SUCCESS, ERROR, WARNING, INFO
  }

  public VANotification(@Nonnull final String message,
                        @Nonnull final NotificationType type) {
    this(message, null, type, 4000);
  }

  public VANotification(@Nonnull final String message,
                        @Nullable final String description,
                        @Nonnull final NotificationType type) {
    this(message, description, type, 4000);
  }

  public VANotification(@Nonnull final String message,
                        @Nullable final String description,
                        @Nonnull final NotificationType type,
                        final int duration) {
    setDuration(duration);
    setPosition(Position.BOTTOM_END);
    buildLayout(message, description, type);
  }

  @Nonnull
  public static VANotification success(@Nonnull final String message) {
    return new VANotification(message, NotificationType.SUCCESS);
  }

  @Nonnull
  public static VANotification error(@Nonnull final String message) {
    return new VANotification(message, NotificationType.ERROR);
  }

  @Nonnull
  public static VANotification warning(@Nonnull final String message) {
    return new VANotification(message, NotificationType.WARNING);
  }

  @Nonnull
  public static VANotification info(@Nonnull final String message) {
    return new VANotification(message, NotificationType.INFO);
  }

  private void buildLayout(@Nonnull final String message,
                           @Nullable final String description,
                           @Nonnull final NotificationType type) {
    final var layout = new Div();
    layout.addClassName("modern-notification");
    layout.addClassName("notification-" + type.name().toLowerCase());

    final var icon = getIconForType(type);
    icon.addClassName("notification-icon");

    final var content = new Div();
    content.addClassName("notification-content");

    final var messageSpan = new Span(message);
    messageSpan.addClassName("notification-message");
    content.add(messageSpan);

    if (description != null && !description.isEmpty()) {
      final var descriptionSpan = new Span(description);
      descriptionSpan.addClassName("notification-description");
      content.add(descriptionSpan);
    }

    final var closeIcon = VaadinIcon.CLOSE_SMALL.create();
    closeIcon.addClassName("notification-close");
    closeIcon.getElement().setAttribute("aria-label", "Close");
    closeIcon.addClickListener(e -> this.close());

    layout.add(icon, content, closeIcon);
    add(layout);
  }

  @Nonnull
  private static Icon getIconForType(@Nonnull final NotificationType type) {
    return switch (type) {
      case SUCCESS -> VaadinIcon.CHECK_CIRCLE.create();
      case ERROR   -> VaadinIcon.CLOSE_CIRCLE.create();
      case WARNING -> VaadinIcon.WARNING.create();
      case INFO    -> VaadinIcon.INFO_CIRCLE.create();
    };
  }
}