package ch.verno.report.base;

import jakarta.annotation.Nonnull;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.StringTemplateResolver;

public abstract class BaseReport<T> {

  @Nonnull protected final TemplateEngine templateEngine;

  public BaseReport(@Nonnull final TemplateEngine templateEngine) {
    this.templateEngine = templateEngine;
  }

  public abstract byte[] generateReportPdf(@Nonnull final T reportData);

  @Nonnull
  protected abstract String getTemplate();

  @Nonnull
  protected TemplateEngine createCustomTemplateEngine() {
    final var resolver = new StringTemplateResolver();
    resolver.setTemplateMode(TemplateMode.HTML);
    resolver.setCacheable(false);

    final var engine = new TemplateEngine();
    engine.setTemplateResolver(resolver);
    return engine;
  }
}
