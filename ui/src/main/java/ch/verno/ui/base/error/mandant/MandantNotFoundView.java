package ch.verno.ui.base.error.mandant;

import ch.verno.common.lib.Routes;
import ch.verno.server.mandant.MandantLookupService;
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
@Route(value = Routes.MANDANT_NOT_FOUND, layout = PublicLayout.class)
public class MandantNotFoundView extends VerticalLayout {

  public MandantNotFoundView(@Nonnull MandantLookupService mandantLookupService) {
    setSpacing(true);
    setPadding(true);
    setMaxWidth("800px");

    final var title = new H2("Mandant nicht gefunden");
    final var description = new Span("Der aufgerufene Mandant existiert nicht. Bitte wähle einen der verfügbaren Mandanten:");
    description.getStyle().setColor("var(--lumo-secondary-text-color)");

    add(title, description);

    final var tenants = mandantLookupService.findAllTenants();

    if (tenants.isEmpty()) {
      final var empty = new Span("Keine Mandanten vorhanden.");
      empty.getStyle().setMarginTop("1rem");
      add(empty);
      return;
    }

    final var list = new VerticalLayout();
    list.setPadding(false);
    list.setSpacing(true);
    list.getStyle().setMarginTop("1.5rem");

    tenants.forEach(mandant -> {
      final var card = new Div();
      card.getStyle()
              .setPadding("0.75rem 1rem")
              .setBorder("1px solid var(--lumo-contrast-10pct)")
              .setBorderRadius("var(--lumo-border-radius-m)")
              .setBackground("var(--lumo-base-color)")
              .setDisplay(Style.Display.FLEX)
              .setFlexDirection(Style.FlexDirection.COLUMN)
              .setGap("0.25rem");

      final var name = new Span(mandant.getName() != null ? mandant.getName() : mandant.getSlug());
      name.getStyle().setFontWeight("600");

      final var meta = new Span("Slug: " + mandant.getSlug() + " · ID: " + mandant.getId());
      meta.getStyle()
              .setFontSize("var(--lumo-font-size-s)")
              .set("color", "var(--lumo-secondary-text-color)");

      card.add(name, meta);
      list.add(card);
    });

    add(list);
  }
}
