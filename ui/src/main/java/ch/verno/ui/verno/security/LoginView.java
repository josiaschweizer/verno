package ch.verno.ui.verno.security;

import ch.verno.ui.base.MainLayout;
import ch.verno.publ.Routes;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route(Routes.LOGIN)
@AnonymousAllowed
public class LoginView extends VerticalLayout {

  public LoginView() {
    final var login = new LoginForm();
    login.setForgotPasswordButtonVisible(false);
    login.setAction("login");

    final var i18n = LoginI18n.createDefault();
    i18n.getForm().setUsername("Username / Email");
    login.setI18n(i18n);

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