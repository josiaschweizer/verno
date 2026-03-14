package ch.verno.ui;

import ch.verno.common.event.ReloadNavigationBarEvent;
import ch.verno.common.gate.GlobalInterface;
import ch.verno.ui.base.billing.SubscriptionApplyService;
import ch.verno.ui.base.components.notification.NotificationStyles;
import ch.verno.ui.base.navigation.MainLayoutSideNavFactory;
import ch.verno.ui.base.settings.UserSettingsApplyService;
import ch.verno.ui.lib.event.bus.ViewEventBus;
import com.google.common.eventbus.Subscribe;
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
  @Nonnull private final UserSettingsApplyService userSettingsApplyService;
  @Nonnull private final SubscriptionApplyService subscriptionApplyService;

  @Nonnull private final Scroller navBarScroller;

  MainLayout(@Nonnull final GlobalInterface globalInterface,
             @Nonnull final UserSettingsApplyService userSettingsApplyService,
             @Nonnull final SubscriptionApplyService subscriptionApplyService) {

    this.sideNavFactory = new MainLayoutSideNavFactory(globalInterface);
    this.userSettingsApplyService = userSettingsApplyService;
    this.subscriptionApplyService = subscriptionApplyService;

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

    userSettingsApplyService.applyCurrentUserSettings();
    subscriptionApplyService.applyCurrentUserSubscriptionState();

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
    final var ui = getUI().orElse(null);
    if (ui == null) {
      return;
    }

    ui.access(() -> navBarScroller.setContent(sideNavFactory.createSideNav()));
  }
}