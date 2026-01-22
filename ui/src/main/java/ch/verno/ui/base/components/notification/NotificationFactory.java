package ch.verno.ui.base.components.notification;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import jakarta.annotation.Nonnull;

@CssImport("./components/notification/va-notification.css")
public final class NotificationFactory {

  public static final Notification.Position NOTIFICATION_POSITION = Notification.Position.BOTTOM_END;
  private static final int DEFAULT_DURATION = 4000;

  private NotificationFactory() {}

  public static void showWarningNotification(@Nonnull final String message) {
    showNotification(message, VaadinIcon.WARNING, NotificationVariant.LUMO_WARNING);
  }

  public static void showSuccessNotification(@Nonnull final String message) {
    showNotification(message, VaadinIcon.CHECK_CIRCLE, NotificationVariant.LUMO_SUCCESS);
  }

  public static void showErrorNotification(@Nonnull final String message) {
    showNotification(message, VaadinIcon.EXCLAMATION_CIRCLE, NotificationVariant.LUMO_ERROR);
  }

  public static void showInfoNotification(@Nonnull final String message) {
    showNotification(message, VaadinIcon.INFO_CIRCLE, NotificationVariant.LUMO_CONTRAST);
  }

  private static void showNotification(@Nonnull final String message,
                                       @Nonnull final VaadinIcon icon,
                                       @Nonnull final NotificationVariant variant) {

    final var notification = new Notification();
    notification.addThemeVariants(variant);
    notification.setDuration(DEFAULT_DURATION);
    notification.setPosition(NOTIFICATION_POSITION);

    final var root = new Div();
    root.addClassName("va-notification");
    root.getElement().getThemeList().add(variant.getVariantName());

    final var accent = new Div();
    accent.addClassName("va-notification__accent");

    final Icon iconComponent = icon.create();
    iconComponent.addClassName("va-notification__icon");

    final var iconWrap = new Div(iconComponent);
    iconWrap.addClassName("va-notification__icon-wrap");

    final var msg = new Span(message);
    msg.addClassName("va-notification__message");

    final var closeIcon = VaadinIcon.CLOSE_SMALL.create();
    closeIcon.addClassName("va-notification__close-icon");

    final var close = new Button(closeIcon, e -> notification.close());
    close.addClassName("va-notification__close");
    close.getElement().setAttribute("aria-label", "Close notification");
    close.getElement().setAttribute("title", "Schließen");

    final var content = new Div(iconWrap, msg, close);
    content.addClassName("va-notification__content");

    root.add(accent, content);
    notification.add(root);
    notification.open();
  }
}