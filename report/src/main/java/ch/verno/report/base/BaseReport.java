package ch.verno.report.base;

import jakarta.annotation.Nonnull;
import org.thymeleaf.TemplateEngine;

public abstract class BaseReport<T> {

  @Nonnull protected final TemplateEngine templateEngine;

  public BaseReport(@Nonnull final TemplateEngine templateEngine) {
    this.templateEngine = templateEngine;
  }

  public abstract byte[] generateReportPdf(@Nonnull final T reportData);

  @Nonnull
  protected abstract String getTemplate();
}
