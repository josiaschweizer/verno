package ch.verno.ui.verno.settings;

import ch.verno.ui.base.settings.BaseSettingsPage;
import ch.verno.ui.lib.Routes;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.Nonnull;
import jakarta.annotation.security.RolesAllowed;

@Route(Routes.MANDANT_SETTINGS)
@PageTitle("Mandant Settings")
@RolesAllowed({"ADMIN", "MANDANT_ADMIN"})
@Menu(order = 98, icon = "vaadin:cog", title = "Mandant Overview")
public class MandantSettings extends BaseSettingsPage {

  public MandantSettings() {

  }

  @Override
  protected void initUI() {

  }

  @Nonnull
  @Override
  protected String getSettingsPageName() {
    return "Mandant Settings";
  }
}
