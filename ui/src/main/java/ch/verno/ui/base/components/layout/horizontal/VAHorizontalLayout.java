package ch.verno.ui.base.components.layout.horizontal;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import jakarta.annotation.Nonnull;

public class VAHorizontalLayout extends HorizontalLayout {

  public VAHorizontalLayout() {
    super();
  }

  public VAHorizontalLayout(@Nonnull final Component... children) {
    super(children);
  }

  public VAHorizontalLayout(@Nonnull final JustifyContentMode justifyContentMode,
                            @Nonnull final Component... children) {
    super(justifyContentMode, children);
  }

  public VAHorizontalLayout(@Nonnull final Alignment alignment,
                            @Nonnull final Component... children) {
    super(alignment, children);
  }

}
