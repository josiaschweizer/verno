package ch.verno.ui.base.components.dialog.stepdialog;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public abstract class BaseDialogStep extends VerticalLayout {

  public abstract boolean isValid();

  public void onBecomeVisible() {
    // can be overridden by subclasses
  }

}
