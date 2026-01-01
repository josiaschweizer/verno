package ch.verno.ui.base;

import ch.verno.common.db.service.IAppUserService;
import ch.verno.server.service.AppUserSettingService;
import ch.verno.ui.base.menu.MenuOrder;
import ch.verno.ui.verno.settings.setting.theme.UISetting;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.Layout;
import com.vaadin.flow.server.menu.MenuConfiguration;
import com.vaadin.flow.server.menu.MenuEntry;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.annotation.security.PermitAll;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Objects;

@Layout
@PermitAll
public final class MainLayout extends AppLayout {

  @Nonnull
  private final IAppUserService appUserService;
  @Nonnull
  private final AppUserSettingService appUserSettingService;

  MainLayout(@Nonnull final IAppUserService appUserService,
             @Nonnull final AppUserSettingService appUserSettingService) {
    this.appUserService = appUserService;
    this.appUserSettingService = appUserSettingService;

    setPrimarySection(Section.DRAWER);
    addClassNames("main-layout");
    addToDrawer(createHeader(), new Scroller(createSideNav()));
  }

  @Override
  protected void onAttach(@Nonnull final AttachEvent attachEvent) {
    super.onAttach(attachEvent);
    loadAndApplyUserSettingsFromDatabase();
  }

  private void loadAndApplyUserSettingsFromDatabase() {
    final var currentUser = getCurrentUser();
    if (currentUser == null) {
      return;
    }

    final var appUser = appUserService.findByUserName(currentUser.getUsername());
    if (appUser.getId() == null) {
      return;
    }

    try {
      final var userSetting = appUserSettingService.getAppUserSettingByUserId(appUser.getId());
      final boolean isDarkMode = "setting.dark".equals(userSetting.getTheme());
      UISetting.applyTheme(isDarkMode);
      UISetting.applyLanguage(userSetting.getLanguage());
    } catch (Exception e) {
      UI.getCurrent().getPage().executeJs("return localStorage.getItem('v-theme');")
              .then(String.class, theme -> {
                if ("setting.dark".equals(theme)) {
                  UISetting.applyTheme(true);
                }
              });
    }
  }

  @Nullable
  private User getCurrentUser() {
    final var authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.getPrincipal() instanceof User) {
      return (User) authentication.getPrincipal();
    }
    return null;
  }

  private Component createHeader() {
    // TODO Replace with real application logo and name
    final var appLogo = VaadinIcon.CUBES.create();
    appLogo.setSize("48px");
    appLogo.setColor("green");

    final var appName = new Span("Verno");
    appName.getStyle().setFontWeight(Style.FontWeight.BOLD);

    final var header = new VerticalLayout(appLogo, appName);
    header.setAlignItems(FlexComponent.Alignment.CENTER);
    header.getStyle().setCursor("pointer");
    header.addClickListener(event -> UI.getCurrent().navigate(""));
    return header;
  }

  @Nonnull
  private SideNav createSideNav() {
    final var sideNav = new SideNav();
    sideNav.addClassNames(LumoUtility.Margin.Horizontal.SMALL);

    final var itemsByOrder = new HashMap<MenuOrder, SideNavItem>();

    MenuConfiguration.getMenuEntries().stream()
            .sorted(Comparator.comparing(e -> MenuOrder.of(e.order())))
            .forEach(entry -> {
              final var order = MenuOrder.of(entry.order());
              final var item = createSideNavItem(entry);

              if (order.depth() == 1) {
                sideNav.addItem(item);
              } else {
                final var parent = itemsByOrder.get(order.parent());
                Objects.requireNonNullElse(parent, sideNav).addItem(item);
              }

              itemsByOrder.put(order, item);
            });

    return sideNav;
  }

  @Nonnull
  private SideNavItem createSideNavItem(@Nonnull final MenuEntry menuEntry) {
    final var title = getTranslation(menuEntry.title());

    final SideNavItem item = (menuEntry.icon() != null)
            ? new SideNavItem(title, menuEntry.path(), new Icon(menuEntry.icon()))
            : new SideNavItem(title, menuEntry.path());

    // so that the grid items isn't highlighted if we are on the detail view
    if (menuEntry.path() != null && menuEntry.path().contains("/detail")) {
      item.setMatchNested(true);
    }

    return item;
  }
}
