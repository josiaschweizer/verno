package ch.verno.ui.verno.dashboard.io.dialog.importing.steps;

import ch.verno.ui.base.dialog.stepdialog.BaseDialogStep;
import ch.verno.ui.verno.dashboard.io.dialog.importing.DialogStep;
import jakarta.annotation.Nonnull;

public record DialogStepDto(@Nonnull DialogStep step,
                            @Nonnull BaseDialogStep content) {
}
