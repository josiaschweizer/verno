package ch.verno.ui.base.layout;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@AnonymousAllowed
public class PublicLayout extends VerticalLayout implements RouterLayout {

  public PublicLayout() {
    setPadding(true);
    setSpacing(true);
    setMaxWidth("900px");
  }

}