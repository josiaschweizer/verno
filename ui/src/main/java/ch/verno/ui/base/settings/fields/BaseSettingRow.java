package ch.verno.ui.base.settings.fields;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public abstract class BaseSettingRow extends HorizontalLayout {

  protected BaseSettingRow(String title, Component field) {
    setWidthFull();
    setAlignItems(Alignment.CENTER);
    setPadding(false);
    setSpacing(true);

    Span titleLabel = new Span(title);

    add(titleLabel, field);
    expand(titleLabel);
  }
}