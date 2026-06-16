package ch.verno.ui.base.components.button;

import ch.verno.ui.base.navigation.Navigator;
import ch.verno.ui.lib.icon.VAIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import jakarta.annotation.Nonnull;

public class ButtonBuilder {

  /**
   * create an icon only button - still needs a tooltip text for a11y
   *
   * @param icon        the component which is on the button
   * @param tooltipText tooltip text required for a11y describing the button
   * @return a instance of VAButton
   */
  @Nonnull
  public static VAButton iconOnly(@Nonnull final VAIcon icon,
                                  @Nonnull final String tooltipText) {
    final var button = new VAButton(icon);
    button.setTooltipText(tooltipText);
    return button;
  }

  /**
   *
   * @param icon
   * @param tooltipText
   * @param href
   * @return
   */
  @Nonnull
  public static VAButton iconHref(@Nonnull final VAIcon icon,
                                  @Nonnull final String tooltipText,
                                  @Nonnull final String href) {
    final var button = iconOnly(icon, tooltipText);
    button.addClickListener(e -> Navigator.navigateTo(href));
    return button;
  }

  /**
   *
   * @param icon
   * @param tooltipText
   * @param viewClass
   * @return
   */
  @Nonnull
  public static VAButton iconVerticalView(@Nonnull final VAIcon icon,
                                          @Nonnull final String tooltipText,
                                          @Nonnull final Class<? extends VerticalLayout> viewClass) {
    final var button = iconOnly(icon, tooltipText);
    button.addClickListener(e -> Navigator.navigateToVertical(viewClass));
    return button;
  }

  /**
   *
   * @param icon
   * @param tooltipText
   * @param viewClass
   * @return
   */
  @Nonnull
  public static VAButton iconHorizontalView(@Nonnull final VAIcon icon,
                                            @Nonnull final String tooltipText,
                                            @Nonnull final Class<? extends HorizontalLayout> viewClass) {
    final var button = iconOnly(icon, tooltipText);
    button.addClickListener(e -> Navigator.navigateToHorizontal(viewClass));
    return button;
  }

}
