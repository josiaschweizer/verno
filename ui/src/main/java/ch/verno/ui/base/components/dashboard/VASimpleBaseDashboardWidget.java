package ch.verno.ui.base.components.dashboard;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import jakarta.annotation.Nonnull;

public class VASimpleBaseDashboardWidget extends VABaseDashboardWidget {

  public VASimpleBaseDashboardWidget(@Nonnull final String title,
                                     @Nonnull final String content,
                                     @Nonnull final String buttonCaption,
                                     @Nonnull final ComponentEventListener<ClickEvent<Button>> listener) {
    super();
    setHeader(title);
    setContent(new Span(content));
    setActions(new Button(buttonCaption, listener));
  }

}
