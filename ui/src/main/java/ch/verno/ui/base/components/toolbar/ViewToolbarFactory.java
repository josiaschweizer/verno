package ch.verno.ui.base.components.toolbar;

import ch.verno.ui.lib.Routes;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import jakarta.annotation.Nonnull;

public class ViewToolbarFactory {

  /**
   * creates a simple ViewToolbar with only a title
   *
   * @param title the title of the toolbar
   * @return a simple ViewToolbar with the specified title
   */
  @Nonnull
  public static ViewToolbar createSimpleToolbar(@Nonnull final String title) {
    return new ViewToolbar(title);
  }

  /**
   * creates a ViewToolbar for a grid view of a specific object type with a create new createButton
   *
   * @param gridObjectName the name of the object displayed in the grid (e.g., "Participant")
   * @return a ViewToolbar configured for a grid view of the specified object type
   */
  @Nonnull
  public static ViewToolbar createGridToolbar(@Nonnull final String gridObjectName) {
    final var url = gridObjectName + "s";

    return createGridToolbar(gridObjectName, url);
  }

  /**
   * creates a ViewToolbar for a grid view of a specific object type with a create new createButton
   *
   * @param gridObjectName the name of the object displayed in the grid (e.g., "Participant")
   * @param url            the URL to navigate to when the create new button is clicked
   * @return a ViewToolbar configured for a grid view of the specified object type
   */
  @Nonnull
  public static ViewToolbar createGridToolbar(@Nonnull final String gridObjectName,
                                              @Nonnull final String url) {
    return new ViewToolbar(gridObjectName + " Grid", createNewButton(gridObjectName, url));
  }

  /**
   * creates a ViewToolbar for a detail view of a specific object type with a create new createButton
   *
   * @param objectName the name of the object displayed in the detail view (e.g., "Participant")
   * @return a ViewToolbar configured for a detail view of the specified object type
   */
  @Nonnull
  public static ViewToolbarResult createDetailToolbar(@Nonnull final String objectName) {
    return createDetailToolbar(objectName, objectName + "s");
  }

  /**
   * creates a ViewToolbar for a detail view of a specific object type with a create new createButton
   *
   * @param objectName the name of the object displayed in the detail view (e.g., "Participant")
   * @param url        the URL to navigate to when the create new button is clicked
   * @return a ViewToolbar configured for a detail view of the specified object type
   */
  @Nonnull
  public static ViewToolbarResult createDetailToolbar(@Nonnull final String objectName,
                                                      @Nonnull final String url) {
    final var newButton = createNewButton(objectName, url);

    return new ViewToolbarResult(
        new ViewToolbar(objectName + " Detail", newButton),
        newButton,
        null
    );
  }

  @Nonnull
  private static Button createNewButton(@Nonnull final String gridObjectName,
                                        @Nonnull final String url) {
    final var createButton = new Button("New " + gridObjectName);
    createButton.addClickListener(event -> UI.getCurrent().navigate(url.toLowerCase()));
    return createButton;
  }

}
