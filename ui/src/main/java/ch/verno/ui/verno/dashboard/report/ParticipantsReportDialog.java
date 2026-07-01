package ch.verno.ui.verno.dashboard.report;

import ch.verno.common.lib.url.UrlUtil;
import ch.verno.contract.gateway.ApiUrl;
import ch.verno.lib.Lazy;
import ch.verno.lib.Publ;
import ch.verno.rpc.client.file.ReportClient;
import ch.verno.rpc.properties.api.ApiConfigProperties;
import ch.verno.ui.base.components.dialog.VAAbstractDialog;
import ch.verno.ui.base.components.file.pdf.PdfPreview;
import ch.verno.ui.base.components.layout.horizontal.VAHorizontalLayout;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.dom.Style;
import jakarta.annotation.Nonnull;
import org.jetbrains.annotations.NonNls;

import java.util.Collection;
import java.util.List;

public class ParticipantsReportDialog extends VAAbstractDialog {

  @NonNls public static final String CLICK_JS = "click";
  @NonNls public static final String DOWNLOAD_ATTRIBUTE = "download";

  @Nonnull private final Lazy<ReportClient> reportClient;
  @Nonnull private final Lazy<ApiConfigProperties> apiConfigProperties;

  @Nonnull private String reportToken;

  @Inject
  public ParticipantsReportDialog(@Nonnull final Injector injector) {
    this.reportClient = Lazy.of(() -> injector.getInstance(ReportClient.class));
    this.apiConfigProperties = Lazy.of(() -> injector.getInstance(ApiConfigProperties.class));

    generateReport();
    initUI(getTranslation("shared.generate.report"));

    setWidth("80%");
    setHeight("90%");

    addDetachListener(e -> deleteTempOnServer());
    addDialogCloseActionListener(e -> deleteTempOnServer());
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

  private void generateReport() {
    reportToken = reportClient.get().generateParticipantsReport();
  }

  @Nonnull
  private Button createDownloadButton() {
    final var hidden = new Anchor(buildAttachmentUrl(reportToken), getTranslation("shared.download"));
    hidden.getElement().setAttribute(DOWNLOAD_ATTRIBUTE, true);
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

  private void deleteTempOnServer() {
    reportClient.get().deleteTempFile(reportToken);
  }

  @Nonnull
  private String buildInlineUrl(@Nonnull final String token) {
    final var baseUrl = apiConfigProperties.get().getBaseUrl();
    final var path = ApiUrl.TEMP_FILE_REPORT + Publ.SLASH + token + ApiUrl.DISPOSITION_INLINE;

    return UrlUtil.buildSafeUrl(baseUrl, path);
  }

  @Nonnull
  private String buildAttachmentUrl(@Nonnull final String token) {
    final var baseUrl = apiConfigProperties.get().getBaseUrl();
    final var path = ApiUrl.TEMP_FILE_REPORT + Publ.SLASH + token + ApiUrl.DISPOSITION_ATTACHMENT;

    return UrlUtil.buildSafeUrl(baseUrl, path);
  }
}