package ch.verno.ui.verno.dashboard.io.dialog.importing;

import jakarta.annotation.Nonnull;

public enum ImportDialogStep {
  ZERO(0),
  ONE(1),
  TWO(2),
  ;

  private final int stepNumber;

  ImportDialogStep(final int stepNumber) {
    this.stepNumber = stepNumber;
  }

  public int getStepNumber() {
    return stepNumber;
  }

  @Nonnull
  public static ImportDialogStep addSteps(final ImportDialogStep currentStep, final int stepsToAdd) {
    int newStepNumber = currentStep.getStepNumber() + stepsToAdd;
    for (ImportDialogStep step : ImportDialogStep.values()) {
      if (step.getStepNumber() == newStepNumber) {
        return step;
      }
    }
    return currentStep;
  }
}
