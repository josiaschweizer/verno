package ch.verno.ui.verno.usermanagemnt.dialog;

import ch.verno.ui.base.dialog.VADialog;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import jakarta.annotation.Nonnull;

import java.util.Collection;
import java.util.List;

public class ChangePasswordDialog extends VADialog {
  @Nonnull
  @Override
  protected HorizontalLayout createContent() {
    return null;
  }

  @Nonnull
  @Override
  protected Collection<Button> createActionButtons() {
    return List.of();
  }
}
