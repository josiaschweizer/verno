package ch.verno.ui.base.components.notification;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.dom.Style;
import jakarta.annotation.Nonnull;

public class NotificationFactory {

  public static final Notification.Position NOTIFICATION_POSITION = Notification.Position.BOTTOM_END;
  private static final int DEFAULT_DURATION = 4000;

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

    final var iconComponent = icon.create();
    iconComponent.getStyle()
            .setFlexShrink("0")
            .setMarginRight("var(--lumo-space-m)");

    final var messageSpan = new Span(message);
    messageSpan.getStyle()
            .setDisplay(Style.Display.FLEX)
            .setAlignItems(Style.AlignItems.CENTER)
            .setFlexGrow("1");

    final var layout = new HorizontalLayout(iconComponent, messageSpan);
    layout.setSpacing(false);
    layout.setAlignItems(FlexComponent.Alignment.CENTER);
    layout.setPadding(true);
    layout.setWidthFull();
    layout.addClassName("notification-content");

    notification.add(layout);
    notification.open();
  }

}
