package ch.verno.ui.verno.dashboard.io.dialog.importing;

import jakarta.annotation.Nonnull;

public enum DialogStep {
  ZERO(0),
  ONE(1),
  TWO(2),
  THREE(3),
  FOUR(4),
  ;

  private final int stepNumber;

  DialogStep(final int stepNumber) {
    this.stepNumber = stepNumber;
  }

  public int getStepNumber() {
    return stepNumber;
  }

  @Nonnull
  public static DialogStep addSteps(final DialogStep currentStep, final int stepsToAdd) {
    int newStepNumber = currentStep.getStepNumber() + stepsToAdd;
    for (DialogStep step : DialogStep.values()) {
      if (step.getStepNumber() == newStepNumber) {
        return step;
      }
    }
    return currentStep;
  }
}
