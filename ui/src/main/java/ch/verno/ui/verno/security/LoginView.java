package ch.verno.ui.verno.security;

import ch.verno.ui.base.MainLayout;
import ch.verno.ui.lib.Routes;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route(Routes.LOGIN)
@AnonymousAllowed
public class LoginView extends VerticalLayout {

  public LoginView() {
    var login = new LoginForm();
    login.setAction("login");

    setSizeFull();
    setPadding(false);
    setSpacing(false);
    setDefaultHorizontalComponentAlignment(Alignment.CENTER);
    setJustifyContentMode(JustifyContentMode.CENTER);

    login.getStyle().set("max-width", "360px");

    add(login);
  }

  @Override
  protected void onAttach(final AttachEvent attachEvent) {
    super.onAttach(attachEvent);

    UI.getCurrent()
            .getChildren()
            .filter(c -> c instanceof MainLayout)
            .map(c -> (MainLayout) c)
            .findFirst()
            .ifPresent(mainLayout -> mainLayout.setDrawerOpened(false));
  }
}