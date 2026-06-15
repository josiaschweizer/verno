package ch.verno.ui.base.components.layout.vertical;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import jakarta.annotation.Nonnull;

public class VAVerticalLayout extends VerticalLayout {

  public VAVerticalLayout() {
    super();
  }

  public VAVerticalLayout(@Nonnull final Component... children) {
    super(children);
  }

  public VAVerticalLayout(@Nonnull final JustifyContentMode justifyContentMode,
                          @Nonnull final Component... children) {
    super(justifyContentMode, children);
  }

  public VAVerticalLayout(@Nonnull final Alignment alignment,
                          @Nonnull final Component... children) {
    super(alignment, children);
  }

}
