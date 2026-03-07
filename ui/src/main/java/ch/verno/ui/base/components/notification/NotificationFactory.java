package ch.verno.ui.base.components.notification;

import ch.verno.ui.base.components.notification.VANotification.NotificationType;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public class NotificationFactory {

  private NotificationFactory() {}

  public static void showSuccessNotification(@Nonnull final String message) {
    VANotification.success(message).open();
  }

  public static void showErrorNotification(@Nonnull final String message) {
    VANotification.error(message).open();
  }

  public static void showWarningNotification(@Nonnull final String message) {
    VANotification.warning(message).open();
  }

  public static void showInfoNotification(@Nonnull final String message) {
    VANotification.info(message).open();
  }

  public static void show(@Nonnull final String message,
                          @Nonnull final NotificationType type) {
    show(message, null, type, 4000);
  }

  public static void show(@Nonnull final String message,
                          @Nullable final String description,
                          @Nonnull final NotificationType type) {
    show(message, description, type, 4000);
  }

  public static void show(@Nonnull final String message,
                          @Nullable final String description,
                          @Nonnull final NotificationType type,
                          final int duration) {
    new VANotification(message, description, type, duration).open();
  }
}