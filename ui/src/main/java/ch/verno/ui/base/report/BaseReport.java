package ch.verno.ui.base.report;

import ar.com.fdvs.dj.domain.builders.DynamicReportBuilder;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.vaadin.reports.PrintPreviewReport;

import java.util.Collections;
import java.util.List;

public abstract class BaseReport<T> extends VerticalLayout {

  @Nonnull
  protected final PrintPreviewReport<T> report;

  @Nonnull
  protected final DynamicReportBuilder reportBuilder;

  private boolean initialized = false;

  protected BaseReport() {
    setPadding(false);
    setSpacing(false);
    setWidthFull();

    report = new PrintPreviewReport<>(getType(), getColumnProperties());
    reportBuilder = report.getReportBuilder();

    configureReport(report, reportBuilder);
  }

  protected void initUI() {
    if (initialized) {
      return;
    }
    initialized = true;

    report.setItems(safeItems(getDataList()));
    addReportToLayout();
  }

  protected void addReportToLayout() {
    add(report);
  }

  public final void refresh() {
    initUI();
    report.setItems(safeItems(getDataList()));
  }

  protected void configureReport(@Nonnull final PrintPreviewReport<T> report,
                                 @Nonnull final DynamicReportBuilder reportBuilder) {
    buildColumns(reportBuilder);
  }

  protected void buildColumns(@Nonnull DynamicReportBuilder reportBuilder) {
    // to be overridden if needed in subclasses
  }

  @Nonnull
  protected abstract List<T> getDataList();

  @Nonnull
  protected abstract Class<T> getType();

  @Nonnull
  protected abstract String[] getColumnProperties();

  @Nonnull
  private static <T> List<T> safeItems(@Nullable final List<T> items) {
    return items == null ? Collections.emptyList() : items;
  }
}