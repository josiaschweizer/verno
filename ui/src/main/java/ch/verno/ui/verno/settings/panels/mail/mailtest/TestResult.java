package ch.verno.ui.verno.settings.panels.mail.mailtest;

import jakarta.annotation.Nonnull;

public record TestResult(
        @Nonnull TestStatus status,
        @Nonnull String message) {

  public boolean isValid() {
    return status == TestStatus.VALID;
  }

}

