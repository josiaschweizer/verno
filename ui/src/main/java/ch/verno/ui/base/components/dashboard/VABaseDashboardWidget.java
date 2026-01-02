package ch.verno.ui.base.components.dashboard;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

@CssImport("./components/dashboard/va-base-dashboard-widget.css")
public class VABaseDashboardWidget extends Div implements HasSize {

  @Nonnull
  private final H3 title; //todo change to component or at least to div

  @Nonnull
  private final Span subtitle;

  @Nonnull
  private final Div actions;

  @Nonnull
  private final Div content;

  public VABaseDashboardWidget() {
    addClassName("va-dashboard-widget");

    final var header = new Div();
    header.addClassName("va-dashboard-widget__header");

    title = new H3();
    title.addClassName("va-dashboard-widget__title");

    subtitle = new Span();
    subtitle.addClassName("va-dashboard-widget__subtitle");

    actions = new Div();
    actions.addClassName("va-dashboard-widget__actions");

    content = new Div();
    content.addClassName("va-dashboard-widget__content");

    final var headerLeft = new Div(title, subtitle);
    headerLeft.addClassName("va-dashboard-widget__header-left");

    header.add(headerLeft, actions);
    header.addClassName("va-dashboard-widget__header-wrapper");
    add(header, content);

    getStyle().set("--va-widget-min-w", "280px");
    getStyle().set("--va-widget-max-w", "1fr");
    getStyle().set("--va-widget-min-h", "160px");
  }

  public void setHeader(@Nullable final String text) {
    title.setText(text != null ? text : "");
  }

  public void setSubheader(@Nullable final String text) {
    subtitle.setText(text != null ? text : "");
    subtitle.setVisible(text != null && !text.isBlank());
  }

  public void setContent(@Nonnull final Component... components) {
    content.removeAll();
    content.add(components);
  }

  public void setActions(@Nonnull final Component... components) {
    actions.removeAll();
    actions.add(components);
  }

  public void setMinWidthCss(@Nonnull final String cssValue) {
    getStyle().set("--va-widget-min-w", cssValue);
  }

  public void setMaxWidthCss(@Nonnull final String cssValue) {
    getStyle().set("--va-widget-max-w", cssValue);
  }

  public void setMinHeightCss(@Nonnull final String cssValue) {
    getStyle().set("--va-widget-min-h", cssValue);
  }

  @Nonnull
  public Div getContentContainer() {
    return content;
  }

  @Nonnull
  public Div getActionsContainer() {
    return actions;
  }
}