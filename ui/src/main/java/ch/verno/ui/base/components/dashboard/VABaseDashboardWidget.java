package ch.verno.ui.base.components.dashboard;

import ch.verno.lib.CssImportConstants;
import ch.verno.lib.Publ;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasSize;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.jetbrains.annotations.NonNls;

@CssImport(CssImportConstants.VA_BASE_DASHBOARD_WIDGET)
public class VABaseDashboardWidget extends Div implements HasSize {

  @NonNls public static final String VA_DASHBOARD_WIDGET_CLASSNAME = "va-dashboard-widget";
  @NonNls public static final String HEADER_CLASSNAME = "va-dashboard-widget__header";
  @NonNls public static final String TITLE_CLASSNAME = "va-dashboard-widget__title";
  @NonNls public static final String SUBTITLE_CLASSNAME = "va-dashboard-widget__subtitle";
  @NonNls public static final String ACTIONS_CLASSNAME = "va-dashboard-widget__actions";
  @NonNls public static final String CONTENT_CLASSNAME = "va-dashboard-widget__content";
  @NonNls public static final String HEADER_LEFT_CLASSNAME = "va-dashboard-widget__header-left";
  @NonNls public static final String HEADER_WRAPPER_CLASSNAME = "va-dashboard-widget__header-wrapper";

  @NonNls public static final String VA_WIDGET_MIN_W_CLASSNAME = "--va-widget-min-w";
  @NonNls public static final String VA_WIDGET_MAX_W_CLASSNAME = "--va-widget-max-w";
  @NonNls public static final String VA_WIDGET_MIN_H_CLASSNAME = "--va-widget-min-h";

  @Nonnull private final H3 title;
  @Nonnull private final Span subtitle;
  @Nonnull private final Div actions;
  @Nonnull private final Div content;

  public VABaseDashboardWidget() {
    addClassName(VA_DASHBOARD_WIDGET_CLASSNAME);

    final var header = new Div();
    header.addClassName(HEADER_CLASSNAME);

    title = new H3();
    title.addClassName(TITLE_CLASSNAME);

    subtitle = new Span();
    subtitle.addClassName(SUBTITLE_CLASSNAME);

    actions = new Div();
    actions.addClassName(ACTIONS_CLASSNAME);

    content = new Div();
    content.addClassName(CONTENT_CLASSNAME);

    final var headerLeft = new Div(title, subtitle);
    headerLeft.addClassName(HEADER_LEFT_CLASSNAME);

    header.add(headerLeft, actions);
    header.addClassName(HEADER_WRAPPER_CLASSNAME);
    add(header, content);

    getStyle().set(VA_WIDGET_MIN_W_CLASSNAME, "280px");
    getStyle().set(VA_WIDGET_MAX_W_CLASSNAME, "1fr");
    getStyle().set(VA_WIDGET_MIN_H_CLASSNAME, "160px");
  }

  public void setHeader(@Nullable final String text) {
    title.setText(text != null ? text : Publ.EMPTY_STRING);
  }

  public void setSubheader(@Nullable final String text) {
    subtitle.setText(text != null ? text : Publ.EMPTY_STRING);
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
    getStyle().set(VA_WIDGET_MIN_W_CLASSNAME, cssValue);
  }

  public void setMaxWidthCss(@Nonnull final String cssValue) {
    getStyle().set(VA_WIDGET_MAX_W_CLASSNAME, cssValue);
  }

  public void setMinHeightCss(@Nonnull final String cssValue) {
    getStyle().set(VA_WIDGET_MIN_H_CLASSNAME, cssValue);
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