package ch.verno.ui.base.report;

import ch.verno.common.lib.api.ApiQueryParam;
import ch.verno.common.lib.url.UrlBuilder;
import ch.verno.common.lib.url.UrlUtil;
import ch.verno.lib.Attributes;
import ch.verno.lib.Lazy;
import ch.verno.rpc.client.file.ReportClient;
import ch.verno.rpc.properties.application.ApplicationProperties;
import ch.verno.ui.base.components.dialog.VAAbstractDialog;
import ch.verno.ui.base.components.file.pdf.PdfPreview;
import ch.verno.ui.base.components.layout.horizontal.VAHorizontalLayout;
import com.google.inject.Injector;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.dom.Style;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.jetbrains.annotations.NonNls;

import java.util.Collection;
import java.util.List;

public abstract class BaseReportPreviewDialog extends VAAbstractDialog {

  @NonNls public static final String CLICK_JS = "click";

  @Nonnull protected final Lazy<ReportClient> reportClient;
  @Nonnull protected final Lazy<ApplicationProperties> applicationProperties;

  @Nullable private String reportToken;
  @Nullable private String apiAccessToken;

  public BaseReportPreviewDialog(@Nonnull final Injector injector) {
    this.reportClient = Lazy.of(() -> injector.getInstance(ReportClient.class));
    this.applicationProperties = Lazy.of(() -> injector.getInstance(ApplicationProperties.class));
  }

  /**
   * Generates the entity-type-specific report (e.g. course report)
   *
   * @return the recently generated file token with which the file can be identified
   */
  @Nonnull
  protected abstract String generateReport();

  /**
   * Return the api controller url to generate the api call to
   */
  @Nonnull
  protected abstract String getApiUrl();

  protected void init() {
    generateTokens();
    initUI(getTranslation("shared.generate.report"));

    setWidth("80%");
    setHeight("90%");

    addDetachListener(e -> deleteTempOnServer());
    addDialogCloseActionListener(e -> deleteTempOnServer());
  }

  protected void generateTokens() {
    this.reportToken = generateReport();
    this.apiAccessToken = reportClient.get().issueAccessToken(reportToken);
  }

  protected void deleteTempOnServer() {
    reportClient.get().deleteTempFile(reportToken);
  }

  @Nonnull
  @Override
  protected VAHorizontalLayout createContent() {
    final var preview = new PdfPreview(buildInlineUrl(reportToken));
    preview.applyDefaultStyle();

    final var layout = new VAHorizontalLayout(preview);
    layout.setSizeFull();
    layout.setPadding(false);
    layout.setSpacing(false);
    return layout;
  }

  @Nonnull
  @Override
  protected Collection<Button> createActionButtons() {
    final var cancelButton = new Button(getTranslation("shared.cancel"), e -> close());
    final var downloadButton = createDownloadButton();

    return List.of(cancelButton, downloadButton);
  }

  @Nonnull
  private Button createDownloadButton() {
    final var hidden = new Anchor(buildAttachmentUrl(reportToken), getTranslation("shared.download"));
    hidden.getElement().setAttribute(Attributes.DOWNLOAD, true);
    hidden.getStyle().setDisplay(Style.Display.NONE);
    add(hidden);

    final var downloadButton = new Button(getTranslation("shared.download"));
    downloadButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    downloadButton.addClickListener(e -> {
      hidden.getElement().callJsFunction(CLICK_JS);
      close();
    });
    return downloadButton;
  }

  @Nonnull
  private String buildInlineUrl(@Nonnull final String token) {
    final var path = UrlUtil.buildUrl(getApiUrl(), token);
    return buildUrl(path, ApiQueryParam.DISPOSITION_INLINE);
  }

  @Nonnull
  private String buildAttachmentUrl(@Nonnull final String token) {
    final var path = UrlUtil.buildUrl(getApiUrl(), token);
    return buildUrl(path, ApiQueryParam.DISPOSITION_ATTACHMENT);
  }

  @Nonnull
  private String buildUrl(@Nonnull final String path, @Nonnull final String disposition) {
    final var rpcUrl = applicationProperties.get().getApiUrl();

    return UrlBuilder.of(rpcUrl, path)
            .withDisposition(disposition)
            .withAccessToken(apiAccessToken)
            .build();
  }
}
