package ch.verno.ui.verno.settings.setting.quantity;

import ch.verno.server.service.MandantSettingService;
import ch.verno.ui.base.settings.VABaseSetting;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import jakarta.annotation.Nonnull;

public class QuantitySetting extends VABaseSetting {

  @Nonnull
  private MandantSettingService mandantSettingService;

  public QuantitySetting(@Nonnull final MandantSettingService mandantSettingService) {
    super("Quantity of Courses per Schedule");
    this.mandantSettingService = mandantSettingService;
  }

  @Nonnull
  @Override
  protected Component createContent() {
    return new VerticalLayout();
  }
}
