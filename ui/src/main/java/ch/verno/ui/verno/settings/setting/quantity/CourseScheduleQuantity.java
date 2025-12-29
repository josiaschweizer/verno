package ch.verno.ui.verno.settings.setting.quantity;

import ch.verno.server.service.MandantSettingService;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import jakarta.annotation.Nonnull;

public class CourseScheduleQuantity extends HorizontalLayout {

  @Nonnull
  private final MandantSettingService mandantSettingService;

  public CourseScheduleQuantity(@Nonnull final MandantSettingService mandantSettingService) {
    this.mandantSettingService = mandantSettingService;
  }

  private void init(){

  }

}
