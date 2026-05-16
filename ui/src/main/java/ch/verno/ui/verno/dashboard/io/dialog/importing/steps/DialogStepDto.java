package ch.verno.ui.verno.dashboard.io.dialog.importing.steps;

import ch.verno.ui.base.components.dialog.stepdialog.BaseDialogStep;
import ch.verno.ui.verno.dashboard.io.dialog.importing.ImportDialogStep;
import jakarta.annotation.Nonnull;

public record DialogStepDto(@Nonnull ImportDialogStep step,
                            @Nonnull BaseDialogStep content) {
}
