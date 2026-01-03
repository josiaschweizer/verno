package ch.verno.ui.base.components.dashboard;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import jakarta.annotation.Nonnull;

public class VASimpleBaseDashboardWidget extends VABaseDashboardWidget {

  private final Button actionButton;

  public VASimpleBaseDashboardWidget(@Nonnull final String title,
                                     @Nonnull final String content,
                                     @Nonnull final String buttonCaption) {
    super();
    setHeader(getTranslation(title));
    setContent(new Span(getTranslation(content)));
    actionButton = new Button(getTranslation(buttonCaption));
    setActions(actionButton);
  }

  public void addActionButtonClickListener(@Nonnull final ComponentEventListener<ClickEvent<Button>> listener) {
    actionButton.addClickListener(listener);
  }

}
