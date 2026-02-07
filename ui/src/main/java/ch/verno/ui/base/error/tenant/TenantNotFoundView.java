package ch.verno.ui.base.error.tenant;

import ch.verno.publ.Routes;
import ch.verno.server.tenant.TenantLookupService;
import ch.verno.ui.base.layout.PublicLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import jakarta.annotation.Nonnull;

@AnonymousAllowed
@Route(value = Routes.TENANT_NOT_FOUND, layout = PublicLayout.class)
public class TenantNotFoundView extends VerticalLayout {

  public TenantNotFoundView(@Nonnull TenantLookupService tenantLookupService) {
    setSpacing(true);
    setPadding(true);
    setMaxWidth("800px");

    final var title = new H2("Tenant not found");
    final var description = new Span("The requested tenant does not exist. Please choose one of the available tenants:");
    description.getStyle().setColor("var(--lumo-secondary-text-color)");

    add(title, description);

    final var tenants = tenantLookupService.findAllTenants();

    if (tenants.isEmpty()) {
      final var empty = new Span("No tenant available.");
      empty.getStyle().setMarginTop("1rem");
      add(empty);
      return;
    }

    final var list = new VerticalLayout();
    list.setPadding(false);
    list.setSpacing(true);
    list.getStyle().setMarginTop("1.5rem");

    tenants.forEach(tenant -> {
      final var card = new Div();
      card.getStyle()
              .setPadding("0.75rem 1rem")
              .setBorder("1px solid var(--lumo-contrast-10pct)")
              .setBorderRadius("var(--lumo-border-radius-m)")
              .setBackground("var(--lumo-base-color)")
              .setDisplay(Style.Display.FLEX)
              .setFlexDirection(Style.FlexDirection.COLUMN)
              .setGap("0.25rem");

      final var name = new Span(tenant.getName() != null ? tenant.getName() : tenant.getSlug());
      name.getStyle().setFontWeight("600");

      final var meta = new Span("Slug: " + tenant.getSlug() + " · ID: " + tenant.getId());
      meta.getStyle()
              .setFontSize("var(--lumo-font-size-s)")
              .set("color", "var(--lumo-secondary-text-color)");

      card.add(name, meta);
      list.add(card);
    });

    add(list);
  }
}
