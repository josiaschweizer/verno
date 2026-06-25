package ch.verno.ui;

import ch.verno.lib.Lazy;
import ch.verno.ui.base.components.notification.NotificationStyles;
import ch.verno.ui.base.navigation.MainLayoutSideNavFactory;
import ch.verno.ui.event.ReloadNavigationBarEvent;
import ch.verno.ui.lib.billing.SubscriptionApplyService;
import ch.verno.ui.lib.event.bus.ViewEventBus;
import ch.verno.ui.lib.settings.UserSettingsApplyService;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Injector;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.router.Layout;
import jakarta.annotation.Nonnull;
import jakarta.annotation.security.PermitAll;

@Layout
@PermitAll
public final class MainLayout extends AppLayout {

  @Nonnull private final MainLayoutSideNavFactory sideNavFactory;
  @Nonnull private final Lazy<UserSettingsApplyService> userSettingsApplyService;
  @Nonnull private final Lazy<SubscriptionApplyService> subscriptionApplyService;

  @Nonnull private final Scroller navBarScroller;

  MainLayout(@Nonnull final Injector injector) {

    this.sideNavFactory = injector.getInstance(MainLayoutSideNavFactory.class);
    this.userSettingsApplyService = Lazy.of(() -> injector.getInstance(UserSettingsApplyService.class));
    this.subscriptionApplyService = Lazy.of(() -> injector.getInstance(SubscriptionApplyService.class));

    setPrimarySection(Section.DRAWER);
    addClassNames("main-layout");

    navBarScroller = new Scroller();
    navBarScroller.setContent(sideNavFactory.createSideNav());

    addToDrawer(
            sideNavFactory.createSideNavHeader(),
            navBarScroller,
            sideNavFactory.createDrawerFooter()
    );

    registerUtilityStyleClasses();
  }

  @Override
  protected void onAttach(@Nonnull final AttachEvent attachEvent) {
    super.onAttach(attachEvent);

    userSettingsApplyService.get().applyCurrentUserSettings();
    subscriptionApplyService.get().applyCurrentUserSubscriptionState();

    ViewEventBus.getInstance().register(this);
  }

  @Override
  protected void onDetach(@Nonnull final DetachEvent detachEvent) {
    ViewEventBus.getInstance().unregister(this);
  }

  private void registerUtilityStyleClasses() {
    addToDrawer(new NotificationStyles());
  }

  @Subscribe
  @SuppressWarnings("unused")
  private void reloadNavigationBar(@Nonnull final ReloadNavigationBarEvent event) {
    getUI().ifPresent(ui -> {
      if (!ui.isAttached()) {
        return;
      }

      ui.access(() -> {
        try {
          final var newSideNav = sideNavFactory.createSideNav();
          navBarScroller.setContent(newSideNav);
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      });
    });
  }
}