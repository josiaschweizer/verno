package ch.verno.ui.base.components.dialog;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.dialog.Dialog;
import jakarta.annotation.Nonnull;

public class VADialog extends Dialog {

  public VADialog() {
    super();
  }

  // close with ignored component event listener for clean lambda for button click listener
  public void close(@SuppressWarnings("unused") @Nonnull final ClickEvent<?> componentEventListener) {
    close();
  }

}
