package ch.verno.ui.base.components.toolbar;

import ch.verno.ui.base.components.filter.VASearchFilter;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public class ViewToolbarFactory {

  @Nonnull
  public static ViewToolbar createSimpleToolbar(@Nonnull final String title) {
    return new ViewToolbar(title);
  }

  @Nonnull
  public static ViewToolbar createGridToolbar(@Nonnull final String gridObjectName) {
    final var url = gridObjectName + "s";

    return createGridToolbar(gridObjectName, url, null);
  }

  @Nonnull
  public static ViewToolbar createGridToolbar(@Nonnull final String gridObjectName,
                                              @Nonnull final String url,
                                              @Nullable final VASearchFilter filter) {
    return new ViewToolbar(gridObjectName + " Grid", filter, createNewButton(gridObjectName, url));
  }

  @Nonnull
  public static ViewToolbarResult createDetailToolbar(@Nonnull final String objectName) {
    return createDetailToolbar(objectName, objectName + "s");
  }

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
