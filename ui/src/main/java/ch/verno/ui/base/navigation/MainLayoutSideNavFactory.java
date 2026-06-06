package ch.verno.ui.base.navigation;

import ch.verno.common.db.service.intern.mail.IMailConfigService;
import ch.verno.common.db.type.mail.MailValidity;
import ch.verno.common.gate.GlobalInterface;
import ch.verno.common.lib.i18n.TranslationHelper;
import ch.verno.publ.Publ;
import ch.verno.publ.Routes;
import ch.verno.ui.lib.icon.CustomIconConstants;
import ch.verno.ui.lib.icon.IconUtil;
import ch.verno.ui.lib.icon.CustomIcons;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.charts.model.Cursor;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.server.menu.MenuConfiguration;
import com.vaadin.flow.server.menu.MenuEntry;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.Nonnull;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class MainLayoutSideNavFactory {

  @Nonnull private final GlobalInterface globalInterface;
  @Nonnull private final IMailConfigService mailConfigService;

  @Nonnull private final List<MenuEntry> menuEntries;

  public MainLayoutSideNavFactory(@Nonnull final GlobalInterface globalInterface) {
    this.globalInterface = globalInterface;
    this.mailConfigService = globalInterface.getService(IMailConfigService.class);

    menuEntries = MenuConfiguration.getMenuEntries();

  }

  @Nonnull
  public Component createSideNavHeader() {
    final var appLogo = new Image("/verno-app.png", "Verno Logo");
    appLogo.setHeight("48px");
    appLogo.addClassNames(LumoUtility.Padding.Top.MEDIUM);
    appLogo.addClassNames(LumoUtility.Padding.Bottom.MEDIUM);

    final var appName = new Span("Verno");
    appName.getStyle().setFontWeight(Style.FontWeight.BOLD);
    appName.getStyle().setFontSize("1.25rem");

    final var header = new VerticalLayout(appLogo, appName);
    header.setPadding(false);
    header.setSpacing(false);
    header.setAlignItems(FlexComponent.Alignment.CENTER);
    header.getStyle().setCursor(Cursor.POINTER.toString());

    header.addClickListener(event -> UI.getCurrent().navigate(""));

    return header;
  }

  @Nonnull
  public SideNav createSideNav() {
    final var sideNav = new SideNav();
    sideNav.addClassNames(LumoUtility.Margin.Horizontal.SMALL);

    final var itemsByOrder = new HashMap<MenuOrder, SideNavItem>();
    menuEntries.stream()
            .filter(this::shouldShowMenuEntry)
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
  public Component createDrawerFooter() {
    final var version = new Span("Version 0.0.1");
    version.getStyle().setFontSize("0.8em");
    version.getStyle().setColor("gray");

    final var footerLayout = new VerticalLayout();
    footerLayout.setPadding(false);
    footerLayout.setSpacing(false);
    footerLayout.setAlignItems(FlexComponent.Alignment.CENTER);
    footerLayout.add(version);

    return footerLayout;
  }

  private boolean shouldShowMenuEntry(@Nonnull final MenuEntry entry) {
    if (isSamePath(Routes.MAIL_LOG, entry.path())) {
      return hasValidMailConfiguration();
    }
    return true;
  }

  private boolean isSamePath(@Nonnull final String route,
                             @Nonnull final String path) {
    final var replacedRoute = route.replaceAll(Publ.SLASH, Publ.EMPTY_STRING);
    final var replacedPath = path.replaceAll(Publ.SLASH, Publ.EMPTY_STRING);
    return replacedRoute.equalsIgnoreCase(replacedPath);
  }

  private boolean hasValidMailConfiguration() {
    try {
      if (!mailConfigService.hasConfigForCurrentTenant()) {
        return false;
      }

      final var mailConfig = mailConfigService.getConfigForCurrentTenant();
      return mailConfig.getMailValidity() == MailValidity.TESTED_VALID;
    } catch (Exception e) {
      return false;
    }
  }

  @Nonnull
  private SideNavItem createSideNavItem(@Nonnull final MenuEntry menuEntry) {
    final var title = TranslationHelper.getTranslation(globalInterface, menuEntry.title());

    final SideNavItem item = (menuEntry.icon() != null)
            ? new SideNavItem(title, menuEntry.path(), createIcon(menuEntry))
            : new SideNavItem(title, menuEntry.path());

    if (menuEntry.path() != null && menuEntry.path().contains("/detail")) {
      item.setMatchNested(true);
    }

    return item;
  }

  @Nonnull
  private Component createIcon(@Nonnull final MenuEntry menuEntry) {
    final var icon = menuEntry.icon();
    if (icon == null) {
      return new Icon(VaadinIcon.CUBES);
    } else if (icon.startsWith(CustomIconConstants.VAADIN_ICON)) {
      return new Icon(icon);
    } else {
      final var customIcon = CustomIcons.of(icon);
      return IconUtil.create(customIcon);
    }
  }
}