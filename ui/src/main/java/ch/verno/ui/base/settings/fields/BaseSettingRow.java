package ch.verno.ui.base.settings.fields;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import jakarta.annotation.Nonnull;

public abstract class BaseSettingRow extends HorizontalLayout {

  protected BaseSettingRow(@Nonnull final String title,
                           @Nonnull final Component field) {
    setWidthFull();
    setAlignItems(Alignment.CENTER);
    setPadding(false);
    setSpacing(true);

    final var titleLabel = new Span(title);

    add(titleLabel, field);
    expand(titleLabel);
  }

  public void setTooltipText(@Nonnull final String tooltipText) {
    getElement().setProperty("title", tooltipText);
  }
}