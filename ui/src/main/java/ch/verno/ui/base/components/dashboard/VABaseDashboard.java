package ch.verno.ui.base.components.dashboard;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import jakarta.annotation.Nonnull;

import java.util.Collection;

@CssImport("./components/dashboard/va-base-dashboard.css")
public class VABaseDashboard extends Composite<Div> {

  @Nonnull
  private final Div grid;

  public VABaseDashboard() {
    grid = new Div();
    grid.addClassName("va-dashboard-grid");

    getContent().add(grid);
  }

  public void addWidget(@Nonnull final VABaseDashboardWidget widget) {
    grid.add(widget);
  }

  public void addWidgets(@Nonnull final Collection<VABaseDashboardWidget> widgets) {
    grid.add(widgets.toArray(VABaseDashboardWidget[]::new));
  }

  public void removeWidget(@Nonnull final VABaseDashboardWidget widget) {
    grid.remove(widget);
  }

  public void clearWidgets() {
    grid.removeAll();
  }

  @Nonnull
  public Div getGrid() {
    return grid;
  }

  public void setGap(@Nonnull final String gapCss) {
    grid.getStyle().set("--va-dashboard-gap", gapCss);
  }

  public void setRowHeight(@Nonnull final String minRowHeightCss) {
    grid.getStyle().set("--va-dashboard-row-min-height", minRowHeightCss);
  }
}