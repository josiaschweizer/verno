package ch.verno.ui.base.error.tenant;

import ch.verno.common.lib.Routes;
import ch.verno.lib.VernoUtility;
import ch.verno.rpc.properties.tenant.TenantProperties;
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

  public TenantNotFoundView(@Nonnull final TenantProperties tenantProperties) {
    setSpacing(true);
    setPadding(true);
    setMaxWidth("800px");

    final var title = new H2("Tenant not found");
    final var description = new Span("The requested tenant does not exist. Please choose one of the available tenants:");
    description.getStyle().setColor(VernoUtility.LUMO_SECONDARY_TEXT_COLOR);

    add(title, description);

    final var tenants = tenantProperties.findAllTenants();

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
              .setBorder(VernoUtility.LUMO_CONTRAST_10_BORDER)
              .setBorderRadius(VernoUtility.LUMO_BORDER_RADIUS_M)
              .setBackground(VernoUtility.LUMO_BASE_COLOR)
              .setDisplay(Style.Display.FLEX)
              .setFlexDirection(Style.FlexDirection.COLUMN)
              .setGap("0.25rem");

      final var name = new Span(tenant.name() != null ? tenant.name() : tenant.slug());
      name.getStyle().setFontWeight(VernoUtility.FONT_WEIGHT_SEMIBOLD);

      final var meta = new Span("Slug: " + tenant.slug() + " - ID: " + tenant.id());
      meta.getStyle().setFontSize(VernoUtility.LUMO_FONT_SIZE_S);
      meta.getStyle().setColor(VernoUtility.LUMO_SECONDARY_TEXT_COLOR);

      card.add(name, meta);
      list.add(card);
    });

    add(list);
  }
}
