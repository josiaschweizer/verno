package ch.verno.ui.verno.dashboard.report;

import ch.verno.common.lib.api.ApiUrl;
import ch.verno.ui.base.report.BaseReportPreviewDialog;
import com.google.inject.Inject;
import com.google.inject.Injector;
import jakarta.annotation.Nonnull;

public class ParticipantsReportDialog extends BaseReportPreviewDialog {

  @Inject
  public ParticipantsReportDialog(@Nonnull final Injector injector) {
    super(injector);

    init();
  }

  @Nonnull
  @Override
  protected String generateReport() {
    return reportClient.get().generateParticipantsReport();
  }


  @Nonnull
  @Override
  protected String getApiUrl() {
    return ApiUrl.TEMP_FILE_REPORT_PUBLIC_AUTH;
  }

}