package ch.verno.ui.base.components.toolbar;

import ch.verno.ui.lib.Routes;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import jakarta.annotation.Nonnull;

public class ViewToolbarFactory {

  /**
   * creates a ViewToolbar for a grid view of a specific object type with a create new createButton
   *
   * @param gridObjectName the name of the object displayed in the grid (e.g., "Participant")
   * @return a ViewToolbar configured for a grid view of the specified object type
   */
  @Nonnull
  public static ViewToolbar createGridToolbar(@Nonnull final String gridObjectName) {
    final var multipleObjectName = gridObjectName + "s";

    return new ViewToolbar(multipleObjectName + " Grid", createNewButton(gridObjectName, multipleObjectName));
  }

  /**
   * creates a ViewToolbar for a detail view of a specific object type with a create new createButton
   *
   * @param objectName the name of the object displayed in the detail view (e.g., "Participant")
   * @return a ViewToolbar configured for a detail view of the specified object type
   */
  @Nonnull
  public static ViewToolbarResult createDetailToolbar(@Nonnull final String objectName) {
    final var newButton = createNewButton(objectName, objectName + "s");

    return new ViewToolbarResult(
        new ViewToolbar(objectName + " Detail", newButton),
        newButton,
        null
    );
  }

  @Nonnull
  private static Button createNewButton(@Nonnull final String gridObjectName,
                                        @Nonnull final String multipleObjectName) {
    final var createButton = new Button("New " + gridObjectName);
    createButton.addClickListener(event -> UI.getCurrent().navigate(multipleObjectName.toLowerCase() + Routes.DETAIL));
    return createButton;
  }

}
