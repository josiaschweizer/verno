package ch.verno.ui.base.components.toolbar;

import ch.verno.common.lib.i18n.TranslationHelper;
import ch.verno.publ.Publ;
import ch.verno.ui.base.components.badge.UserActionBadge;
import ch.verno.ui.base.components.filter.VASearchFilter;
import ch.verno.common.lib.Routes;
import ch.verno.ui.lib.helper.LogoutHelper;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

public class ViewToolbarFactory {

  @Nonnull
  public static ViewToolbar createSimpleToolbar(@Nonnull final String title) {
    final var viewToolbar = new ViewToolbar(title);
    applyUserBadgeToToolbar(viewToolbar);
    return viewToolbar;
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
    final var translation = TranslationHelper.getTranslation("base.grid", UI.getCurrent().getLocale());

    if (filter != null) {
      return new ViewToolbar(
              gridObjectName + Publ.SPACE + translation,
              filter,
              createNewButton(gridObjectName, url)
      );
    }

    final var viewToolbar = new ViewToolbar(gridObjectName + Publ.SPACE + translation, createNewButton(gridObjectName, url));
    applyUserBadgeToToolbar(viewToolbar);
    return viewToolbar;
  }

  @Nonnull
  public static ViewToolbarResult createDetailToolbar(@Nonnull final String objectName,
                                                      @Nonnull final String url) {
    final var newButton = createNewButton(objectName, url);

    final var translation = TranslationHelper.getTranslation("base.detail", UI.getCurrent().getLocale());

    final var viewToolbar = new ViewToolbar(objectName + Publ.SPACE + translation, newButton);
    applyUserBadgeToToolbar(viewToolbar);

    return new ViewToolbarResult(
            viewToolbar,
            newButton,
            null
    );
  }

  @Nonnull
  private static Button createNewButton(@Nonnull final String gridObjectName,
                                        @Nonnull final String url) {
    final var translation = TranslationHelper.getTranslation("common.new", UI.getCurrent().getLocale());

    final var createButton = new Button(translation, VaadinIcon.PLUS.create());
    createButton.addClickListener(event -> UI.getCurrent().navigate(url.toLowerCase()));
    return createButton;
  }

  private static void applyUserBadgeToToolbar(@Nonnull final ViewToolbar toolbar) {
    final var currentUser = getCurrentUser();
    if (currentUser == null) {
      return;
    }

    final var ui = UI.getCurrent();
    final var userBadge = new UserActionBadge(currentUser.getUsername())
//            .addItem(VaadinIcon.USER, "Profil", () -> ui.navigate(Routes.PROFILE))
            .addItemWithTranslationKey(VaadinIcon.SLIDER, "setting.user_settings", () -> ui.navigate(Routes.USER_SETTINGS))
            .addItemWithTranslationKey(VaadinIcon.SIGN_OUT, "shared.logout", LogoutHelper::logout);

    toolbar.addUserAction(userBadge);
  }

  @Nullable
  private static User getCurrentUser() {
    final var authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.User) {
      return (User) authentication.getPrincipal();
    }
    return null;
  }

}
