package ch.verno.ui.base.components.badge;

import com.vaadin.flow.component.html.Span;
import jakarta.annotation.Nonnull;

public class VABadgeLabel extends Span {

  public VABadgeLabel(@Nonnull final String label,
                      @Nonnull final ch.verno.common.base.components.badge.VABadgeLabelOptions option) {
    super(label);
    this.getElement().getThemeList().add(option.getTheme());
  }
}