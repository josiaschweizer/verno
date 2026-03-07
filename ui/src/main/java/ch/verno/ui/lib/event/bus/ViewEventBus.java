package ch.verno.ui.lib.event.bus;

import com.google.common.eventbus.EventBus;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinSession;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ViewEventBus extends EventBus {

  private static final Logger LOG = LoggerFactory.getLogger(ViewEventBus.class);
  private static final String SESSION_ATTRIBUTE_KEY = ViewEventBus.class.getName();

  public ViewEventBus() {
    super((exception, context) ->
            LOG.error(
                    "Error dispatching event [{}] to subscriber method [{}]",
                    context.getEvent(),
                    context.getSubscriberMethod(),
                    exception
            )
    );
  }

  /**
   * Returns the session-specific {@link ViewEventBus} for the current Vaadin session.
   * <p>
   * If no instance exists yet, a new one is created, stored in the session,
   * and returned.
   *
   * @return the session-specific event bus
   * @throws IllegalStateException if no active {@link VaadinSession} is available
   */
  @Nonnull
  public static ViewEventBus getInstance() {
    final var session = requireCurrentSession();

    ViewEventBus eventBus = (ViewEventBus) session.getAttribute(SESSION_ATTRIBUTE_KEY);
    if (eventBus == null) {
      eventBus = new ViewEventBus();
      session.setAttribute(SESSION_ATTRIBUTE_KEY, eventBus);
      LOG.debug("Created new ViewEventBus for session {}", session.getSession().getId());
    }

    return eventBus;
  }

  /**
   * Returns the session-specific {@link ViewEventBus} for the current Vaadin session,
   * or {@code null} if no session is currently active.
   *
   * @return the session-specific event bus, or {@code null} if no session exists
   */
  @Nullable
  public static ViewEventBus getSessionInstanceOrNull() {
    final VaadinSession session = VaadinSession.getCurrent();
    if (session == null) {
      return null;
    }

    return (ViewEventBus) session.getAttribute(SESSION_ATTRIBUTE_KEY);
  }

  /**
   * Registers the given listener and automatically unregisters it when the
   * provided {@link UI} is detached.
   *
   * @param listener the listener to register
   * @param ui       the UI that controls the listener lifecycle
   */
  public void registerWithUi(@Nonnull final Object listener, @Nonnull final UI ui) {
    register(listener);

    ui.addDetachListener(event -> {
      unregister(listener);
      LOG.debug("Automatically unregistered listener [{}] from ViewEventBus", listener.getClass().getSimpleName());
    });
  }

  /**
   * Posts the given event using {@link UI#access(Runnable)} so that event
   * dispatching is executed in the UI thread.
   *
   * @param event the event to post
   * @param ui    the UI used for thread-safe access
   */
  public void postToUi(@Nonnull final Object event, @Nonnull final UI ui) {
    ui.access(() -> post(event));
  }

  @Nonnull
  private static VaadinSession requireCurrentSession() {
    final var session = VaadinSession.getCurrent();
    if (session == null) {
      throw new IllegalStateException("No active VaadinSession found");
    }
    return session;
  }
}