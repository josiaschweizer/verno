package ch.verno.ui.base.factory;

import ch.verno.common.db.enums.CourseScheduleStatus;
import ch.verno.ui.base.components.badge.VABadgeLabel;
import ch.verno.common.base.components.badge.VABadgeLabelOptions;
import jakarta.annotation.Nonnull;

public class BadgeLabelFactory {

  @Nonnull
  public static VABadgeLabel createBadgeLabelFromStatus(@Nonnull final String labelText,
                                                        @Nonnull final CourseScheduleStatus status) {
    if (status == CourseScheduleStatus.PLANNED) {
      return new VABadgeLabel(labelText, VABadgeLabelOptions.NORMAL);
    } else if (status == CourseScheduleStatus.ACTIVE) {
      return new VABadgeLabel(labelText, VABadgeLabelOptions.SUCCESS);
    } else {
      return new VABadgeLabel(labelText, VABadgeLabelOptions.CONTRAST);
    }
  }

  @Nonnull
  public static VABadgeLabel createBadgeLabel(@Nonnull final String labelText,
                                              @Nonnull final VABadgeLabelOptions option) {
    return new VABadgeLabel(labelText, option);
  }

}
