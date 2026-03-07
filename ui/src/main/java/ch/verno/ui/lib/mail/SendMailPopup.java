package ch.verno.ui.lib.mail;

import ch.verno.common.tenant.TenantContext;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.server.Command;
import jakarta.annotation.Nonnull;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class SendMailPopup extends Dialog {

  public SendMailPopup(@Nonnull final String titleText,
                       @Nonnull final String loadingText) {
    setCloseOnEsc(false);
    setCloseOnOutsideClick(false);

    final var progressBar = new ProgressBar();
    progressBar.setIndeterminate(true);
    progressBar.setWidthFull();

    final var layout = new VerticalLayout(
            new H3(titleText),
            new Span(loadingText),
            progressBar
    );
    layout.setPadding(true);
    layout.setSpacing(true);
    layout.setWidth("520px");
    add(layout);
  }

  public void openAndRunAsync(@Nonnull final UI ui,
                              @Nonnull final Long tenantId,
                              @Nonnull final Executor executor,
                              @Nonnull final Runnable task,
                              @Nonnull final Command onSuccessUi,
                              @Nonnull final Command onErrorUi) {
    open();

    CompletableFuture
            .runAsync(() -> {
              TenantContext.set(tenantId);

              try {
                task.run();
              } finally {
                TenantContext.clear();
              }
            }, executor)
            .whenComplete((ignored, throwable) -> ui.access(() -> {
              try {
                close();

                if (throwable == null) {
                  onSuccessUi.execute();
                } else {
                  onErrorUi.execute();
                }
              } finally {
                ui.push();
              }
            }));
  }
}