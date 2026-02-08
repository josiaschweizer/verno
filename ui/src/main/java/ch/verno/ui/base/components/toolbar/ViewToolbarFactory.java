package ch.verno.ui.base.components.toolbar;

import ch.verno.common.gate.GlobalInterface;
import ch.verno.common.lib.i18n.TranslationHelper;
import ch.verno.publ.Publ;
import ch.verno.publ.Routes;
import ch.verno.ui.base.components.badge.UserActionBadge;
import ch.verno.ui.base.components.filter.VASearchFilter;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

public class ViewToolbarFactory {

  @Nonnull
  public static ViewToolbar createSimpleToolbar(@Nonnull final GlobalInterface globalInterface,
                                                @Nonnull final String title) {
    final var viewToolbar = new ViewToolbar(title);
    applyUserBadgeToToolbar(globalInterface, viewToolbar);
    return viewToolbar;
  }

  @Nonnull
  public static ViewToolbar createGridToolbar(@Nonnull final GlobalInterface globalInterface,
                                              @Nonnull final String gridObjectName,
                                              @Nonnull final String url) {
    return createGridToolbar(globalInterface, gridObjectName, createNewButton(globalInterface, url), null);
  }

  @Nonnull
  public static ViewToolbar createGridToolbar(@Nonnull final GlobalInterface globalInterface,
                                              @Nonnull final String gridObjectName,
                                              @Nonnull final Runnable onCreateAction) {
    return createGridToolbar(globalInterface, gridObjectName, createNewButton(globalInterface, onCreateAction), null);
  }

  @Nonnull
  public static ViewToolbar createGridToolbar(@Nonnull final GlobalInterface globalInterface,
                                              @Nonnull final String gridObjectName,
                                              @Nonnull final Button actionButton,
                                              @Nullable final VASearchFilter filter) {
    final var translation = TranslationHelper.getTranslation(globalInterface, "base.grid");

    if (filter != null) {
      return new ViewToolbar(
              gridObjectName + Publ.SPACE + translation,
              filter,
              actionButton
      );
    }

    final var viewToolbar = new ViewToolbar(gridObjectName + Publ.SPACE + translation, actionButton);
    applyUserBadgeToToolbar(globalInterface, viewToolbar);
    return viewToolbar;
  }

  @Nonnull
  public static ViewToolbarResult createDetailToolbar(@Nonnull final GlobalInterface globalInterface,
                                                      @Nonnull final String objectName,
                                                      @Nonnull final String url) {
    final var newButton = createNewButton(globalInterface, url);

    final var translation = TranslationHelper.getTranslation(globalInterface, "base.detail");
    final var viewToolbar = new ViewToolbar(objectName + Publ.SPACE + translation, newButton);
    applyUserBadgeToToolbar(globalInterface, viewToolbar);

    return new ViewToolbarResult(
            viewToolbar,
            newButton,
            null
    );
  }

  @Nonnull
  private static Button createNewButton(@Nonnull final GlobalInterface globalInterface,
                                        @Nonnull final String url) {
    final var createButton = createButton(globalInterface);
    createButton.addClickListener(event -> UI.getCurrent().navigate(url.toLowerCase()));
    return createButton;
  }

  @Nonnull
  private static Button createNewButton(@Nonnull final GlobalInterface globalInterface,
                                        @Nonnull final Runnable onCreateAction) {
    final var createButton = createButton(globalInterface);
    createButton.addClickListener(event -> onCreateAction.run());
    return createButton;
  }

  @Nonnull
  private static Button createButton(@Nonnull final GlobalInterface globalInterface) {
    final var translation = TranslationHelper.getTranslation(globalInterface, "common.new");
    return new Button(translation, VaadinIcon.PLUS.create());
  }

  private static void applyUserBadgeToToolbar(@Nonnull final GlobalInterface globalInterface,
                                              @Nonnull final ViewToolbar toolbar) {
    final var currentUser = getCurrentUser();
    if (currentUser == null) {
      return;
    }

    final var ui = UI.getCurrent();
    final var userBadge = new UserActionBadge(currentUser.getUsername())
//            .addItem(VaadinIcon.USER, "Profil", () -> ui.navigate(Routes.PROFILE))
            .addItemWithTranslationKey(VaadinIcon.SLIDER, "setting.user_settings", () -> ui.navigate(Routes.USER_SETTINGS))
            .addItemWithTranslationKey(VaadinIcon.SIGN_OUT, "shared.logout", globalInterface::logout);

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
