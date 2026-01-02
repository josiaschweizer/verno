package ch.verno.ui.base.components.toolbar;

import ch.verno.common.util.Publ;
import ch.verno.ui.base.components.filter.VASearchFilter;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.i18n.I18NProvider;
import com.vaadin.flow.server.VaadinService;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public class ViewToolbarFactory {

  @Nonnull
  public static ViewToolbar createSimpleToolbar(@Nonnull final String title) {
    return new ViewToolbar(title);
  }

  @Nonnull
  public static ViewToolbar createGridToolbar(@Nonnull final String gridObjectName) {
    final var url = gridObjectName + Publ.S;

    return createGridToolbar(gridObjectName, url, null);
  }

  @Nonnull
  public static ViewToolbar createGridToolbar(@Nonnull final String gridObjectName,
                                              @Nonnull final String url) {
    return createGridToolbar(gridObjectName, url, null);
  }

  @Nonnull
  public static ViewToolbar createGridToolbar(@Nonnull final String gridObjectName,
                                              @Nonnull final String url,
                                              @Nullable final VASearchFilter filter) {
    final var translation = getI18NProvider() != null ?
            getI18NProvider().getTranslation("base.grid", UI.getCurrent().getLocale()) :
            "Grid";

    if (filter != null) {
      return new ViewToolbar(
              gridObjectName + Publ.SPACE + translation,
              filter,
              createNewButton(gridObjectName, url)
      );
    }

    return new ViewToolbar(gridObjectName + Publ.SPACE + translation, createNewButton(gridObjectName, url));
  }

  @Nonnull
  public static ViewToolbarResult createDetailToolbar(@Nonnull final String objectName) {
    return createDetailToolbar(objectName, objectName + Publ.S);
  }

  @Nonnull
  public static ViewToolbarResult createDetailToolbar(@Nonnull final String objectName,
                                                      @Nonnull final String url) {
    final var newButton = createNewButton(objectName, url);

    final var translation = getI18NProvider() != null ?
            getI18NProvider().getTranslation("base.detail", UI.getCurrent().getLocale()) :
            "Detail";

    return new ViewToolbarResult(
            new ViewToolbar(objectName + Publ.SPACE + translation, newButton),
            newButton,
            null
    );
  }

  @Nonnull
  private static Button createNewButton(@Nonnull final String gridObjectName,
                                        @Nonnull final String url) {
    final var translation = getI18NProvider() != null ?
            getI18NProvider().getTranslation("common.new", UI.getCurrent().getLocale()) :
            "New";

    final var createButton = new Button(translation + Publ.SPACE + gridObjectName);
    createButton.addClickListener(event -> UI.getCurrent().navigate(url.toLowerCase()));
    return createButton;
  }

  @Nullable
  protected static I18NProvider getI18NProvider() {
    final var service = VaadinService.getCurrent();
    if (service != null && service.getInstantiator() != null) {
      return service.getInstantiator().getI18NProvider();
    }

    return null;
  }


}
