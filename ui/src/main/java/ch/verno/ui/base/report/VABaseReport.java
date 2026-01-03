package ch.verno.ui.base.report;

import ar.com.fdvs.dj.domain.Style;
import ar.com.fdvs.dj.domain.constants.Font;
import ar.com.fdvs.dj.domain.constants.HorizontalAlign;
import ar.com.fdvs.dj.domain.constants.Page;
import ar.com.fdvs.dj.domain.entities.columns.AbstractColumn;
import jakarta.annotation.Nonnull;

import java.util.Objects;

public abstract class VABaseReport<T> extends BaseReport<T> {

  private static final String PDF_FONT = "SansSerif";

  public enum ReportHorizontalAlignment {
    LEFT(HorizontalAlign.LEFT),
    CENTER(HorizontalAlign.CENTER),
    RIGHT(HorizontalAlign.RIGHT);

    private final HorizontalAlign dj;

    ReportHorizontalAlignment(HorizontalAlign dj) {
      this.dj = dj;
    }
  }

  @Nonnull
  private String title = "";

  @Nonnull
  private String subtitle = "";

  @Nonnull
  private ReportHorizontalAlignment titleAlignment = ReportHorizontalAlignment.LEFT;

  @Nonnull
  private ReportHorizontalAlignment subtitleAlignment = ReportHorizontalAlignment.LEFT;

  protected VABaseReport() {
    applyDefaultReportStyles();
  }

  public final void setTitle(@Nonnull final String title) {
    this.title = Objects.requireNonNull(title);
    reportBuilder.setTitle(this.title);
  }

  public final void setSubtitle(@Nonnull final String subtitle) {
    this.subtitle = Objects.requireNonNull(subtitle);
    reportBuilder.setSubtitle(this.subtitle);
  }

  public final void setTitleAlignment(@Nonnull final ReportHorizontalAlignment alignment) {
    this.titleAlignment = Objects.requireNonNull(alignment);
    applyDefaultReportStyles();
  }

  public final void setSubtitleAlignment(@Nonnull final ReportHorizontalAlignment alignment) {
    this.subtitleAlignment = Objects.requireNonNull(alignment);
    applyDefaultReportStyles();
  }

  protected final void addColumn(@Nonnull final AbstractColumn column) {
    reportBuilder.addColumn(column);
  }

  private static final String PDF_FONT_HELVETICA = "Helvetica";

  protected void applyDefaultReportStyles() {
    reportBuilder.setPageSizeAndOrientation(Page.Page_A4_Landscape());
    reportBuilder.setMargins(20, 20, 20, 20);
    reportBuilder.setUseFullPageWidth(true);

    reportBuilder.setTitle(title);
    reportBuilder.setSubtitle(subtitle);

    final var titleStyle = new Style();
    final var titleFont = new Font(14, PDF_FONT, PDF_FONT, null, false);
    titleFont.setBold(true);
    titleStyle.setFont(titleFont);
    titleStyle.setHorizontalAlign(titleAlignment.dj);

    final var subtitleStyle = new Style();
    subtitleStyle.setFont(new Font(10, PDF_FONT, PDF_FONT, null, false));
    subtitleStyle.setHorizontalAlign(subtitleAlignment.dj);

    final var headerStyle = new Style();
    final var headerFont = new Font(10, PDF_FONT, PDF_FONT, null, false);
    headerFont.setBold(true);
    headerStyle.setFont(headerFont);
    headerStyle.setHorizontalAlign(HorizontalAlign.LEFT);

    final var detailStyle = new Style();
    detailStyle.setFont(new Font(9, PDF_FONT, PDF_FONT, null, false));
    detailStyle.setHorizontalAlign(HorizontalAlign.LEFT);

    reportBuilder.setDefaultStyles(titleStyle, subtitleStyle, headerStyle, detailStyle);
  }
}