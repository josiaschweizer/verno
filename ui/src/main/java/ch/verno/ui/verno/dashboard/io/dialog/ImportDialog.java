package ch.verno.ui.verno.dashboard.io.dialog;

import ch.verno.common.gate.VernoApplicationGate;
import ch.verno.ui.verno.dashboard.io.dialog.steps.DialogStepDto;
import ch.verno.ui.verno.dashboard.io.dialog.steps.step1.ImportFile;
import ch.verno.ui.verno.dashboard.io.dialog.steps.step2.ImportMapping;
import ch.verno.ui.verno.dashboard.io.widgets.ImportEntityConfig;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ImportDialog extends Dialog {

  @Nonnull private final List<DialogStepDto> steps;

  @Nullable private HorizontalLayout contentLayout;
  @Nullable private Button forwardButton;
  @Nullable private Button finishButton;
  @Nullable private Button backButton;

  private DialogStep currentStep;

  public ImportDialog(@Nonnull final VernoApplicationGate vernoApplicationGate,
                      @Nonnull final String dialogTitle,
                      @Nonnull final ImportEntityConfig entityConfig) {
    steps = new ArrayList<>();
    currentStep = DialogStep.ZERO;

    final var importFileStep = new ImportFile(vernoApplicationGate);
    final var importMappingStep = new ImportMapping(vernoApplicationGate, entityConfig);

    importFileStep.setOnFileUploadedListener(this::updateButtonVisibility);
    importMappingStep.setOnValidationChangedListener(this::updateButtonVisibility);

    steps.add(new DialogStepDto(DialogStep.ONE, importFileStep));
    steps.add(new DialogStepDto(DialogStep.TWO, importMappingStep));

    initUI(dialogTitle);
  }

  private void initUI(@NonNull final String dialogTitle) {
    setHeight("90vh");
    setWidth("min(1500px, 95vw)");
    setMaxWidth("1500px");
    setMinWidth("320px");

    updateHeaderTitle(dialogTitle);
    add(createContent());
    createActionButtons().forEach(btn -> getFooter().add(btn));
  }

  @NonNull
  protected HorizontalLayout createContent() {
    contentLayout = new HorizontalLayout();
    contentLayout.setSizeFull();
    contentLayout.setPadding(true);
    contentLayout.setSpacing(true);

    updateContentByStep(DialogStep.ONE, contentLayout);

    return contentLayout;
  }


  @Nonnull
  protected Collection<Button> createActionButtons() {
    final var cancelButton = new Button(getTranslation("shared.cancel"), e -> close());

//    backButton = new Button(getTranslation("shared.back"), e -> {
//      if (contentLayout == null) {
//        return;
//      }
//
//      final var previousStep = DialogStep.addSteps(currentStep, -1);
//      if (previousStep.getStepNumber() > 0) {
//        updateContentByStep(previousStep, contentLayout);
//      }
//    });

    forwardButton = new Button(getTranslation("shared.forward"), e -> {
      if (contentLayout == null) {
        return;
      }

      final var nextStep = DialogStep.addSteps(currentStep, 1);

      if (currentStep == DialogStep.ONE && nextStep == DialogStep.TWO) {
        final var importFileStep = steps.stream()
                .filter(s -> s.step() == DialogStep.ONE)
                .findFirst();

        final var importMappingStep = steps.stream()
                .filter(s -> s.step() == DialogStep.TWO)
                .findFirst();

        if (importFileStep.isPresent() && importMappingStep.isPresent()) {
          final var importFile = (ImportFile) importFileStep.get().content();
          final var importMapping = (ImportMapping) importMappingStep.get().content();

          if (importFile.hasFile()) {
            final var fileToken = importFile.getTempToken();
            importMapping.setFileToken(fileToken);
          }
        }
      }

      updateContentByStep(nextStep, contentLayout);
    });

    finishButton = new Button(getTranslation("shared.finish"), e -> {
      final var importMappingStep = steps.stream()
              .filter(s -> s.step() == DialogStep.TWO)
              .findFirst();

      if (importMappingStep.isPresent()) {
        final var importMapping = (ImportMapping) importMappingStep.get().content();
        if (importMapping.performImport()) {
          close();
        }
      }
    });

    updateButtonVisibility();

    return List.of(cancelButton, forwardButton, finishButton);
  }

  private void updateButtonVisibility() {
    if (forwardButton == null || finishButton == null) {
      return;
    }

    if (currentStep.getStepNumber() == steps.size()) {
      forwardButton.setVisible(false);
      finishButton.setVisible(true);
    } else {
      forwardButton.setVisible(true);
      finishButton.setVisible(false);
    }

    final var first = steps.stream()
            .filter(s -> s.step() == currentStep)
            .findFirst();
    if (first.isEmpty()) {
      return;
    }

    final var content = first.get().content();
    forwardButton.setEnabled(content.isValid());
    finishButton.setEnabled(content.isValid());
  }

  protected void updateContentByStep(@Nonnull final DialogStep stepIndex,
                                     @Nonnull final HorizontalLayout contentLayout) {
    final var first = steps.stream()
            .filter(s -> s.step() == stepIndex)
            .findFirst();
    if (first.isEmpty()) {
      return;
    }

    currentStep = stepIndex;
    final var content = first.get().content();

    contentLayout.removeAll();
    contentLayout.add(content);
    contentLayout.expand(content);
    content.onBecomeVisible();

    updateButtonVisibility();
  }

  private void updateHeaderTitle(@NonNull final String title) {
    setHeaderTitle(title);
  }
}
