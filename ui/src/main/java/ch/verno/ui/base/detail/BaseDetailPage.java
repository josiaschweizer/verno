package ch.verno.ui.base.detail;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import jakarta.annotation.Nonnull;

public abstract class BaseDetailPage<T> extends VerticalLayout implements HasUrlParameter<Long> {


  @Nonnull
  private HorizontalLayout createLayoutFromComponents(@Nonnull final Component... components) {
    final var layout = new HorizontalLayout();
    layout.setWidthFull();

    for (final var component : components) {
      layout.add(component);
    }

    return layout;
  }

  @Override
  public void setParameter(@Nonnull final BeforeEvent event,
                           @Nonnull final Long parameter) {

  }
}
