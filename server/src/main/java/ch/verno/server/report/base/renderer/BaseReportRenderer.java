package ch.verno.server.report.base.renderer;

import jakarta.annotation.Nonnull;
import org.thymeleaf.TemplateEngine;

public abstract class BaseReportRenderer<T> {

  @Nonnull protected final TemplateEngine templateEngine;

  public BaseReportRenderer(@Nonnull final TemplateEngine templateEngine) {
    this.templateEngine = templateEngine;
  }

  public abstract byte[] renderReportPdf(@Nonnull final T reportData);

}
