package ch.verno.ui.base.components.toolbar;

import ch.verno.lib.Publ;
import ch.verno.ui.base.components.badge.UserActionBadge;
import ch.verno.ui.base.components.button.VAButton;
import ch.verno.ui.base.components.filter.VASearchFilter;
import ch.verno.ui.i18n.TranslationHelper;
import ch.verno.ui.injection.Injector;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public class ViewToolbarFactory {

  @Nonnull
  public static ViewToolbar createSimpleToolbar(@Nonnull final Injector injector,
                                                @Nonnull final String title) {
    final var viewToolbar = new ViewToolbar(title);
    applyUserBadgeToToolbar(injector, viewToolbar);
    return viewToolbar;
  }

  @Nonnull
  public static ViewToolbar createGridToolbar(@Nonnull final Injector injector,
                                              @Nonnull final String gridObjectName) {
    return createGridToolbar(injector, gridObjectName, null, null);
  }

  @Nonnull
  public static ViewToolbar createGridToolbar(@Nonnull final Injector injector,
                                              @Nonnull final String gridObjectName,
                                              @Nonnull final String url) {
    return createGridToolbar(injector, gridObjectName, createNewButton(injector, url), null);
  }

  @Nonnull
  public static ViewToolbar createGridToolbar(@Nonnull final Injector injector,
                                              @Nonnull final String gridObjectName,
                                              @Nonnull final Runnable onCreateAction) {
    return createGridToolbar(injector, gridObjectName, createNewButton(injector, onCreateAction), null);
  }

  @Nonnull
  public static ViewToolbar createGridToolbar(@Nonnull final Injector injector,
                                              @Nonnull final String gridObjectName,
                                              @Nullable final Button actionButton,
                                              @Nullable final VASearchFilter filter) {
    final var translation = TranslationHelper.getTranslation(injector, "base.grid");

    if (filter != null) {
      return new ViewToolbar(
              gridObjectName + Publ.SPACE + translation,
              filter,
              actionButton
      );
    }

    ViewToolbar viewToolbar;
    if (actionButton != null) {
      viewToolbar = new ViewToolbar(gridObjectName + Publ.SPACE + translation, actionButton);
    } else {
      viewToolbar = new ViewToolbar(gridObjectName + Publ.SPACE + translation);
    }

    applyUserBadgeToToolbar(injector, viewToolbar);
    return viewToolbar;
  }

  @Nonnull
  public static ViewToolbarResult createDetailToolbar(@Nonnull final Injector injector,
                                                      @Nonnull final String objectName,
                                                      @Nonnull final String url) {
    final var newButton = createNewButton(injector, url);

    final var translation = injector.getInstance(TranslationHelper.class).getTranslation("base.detail");
    final var viewToolbar = new ViewToolbar(objectName + Publ.SPACE + translation, newButton);
    applyUserBadgeToToolbar(injector, viewToolbar);

    return new ViewToolbarResult(
            viewToolbar,
            newButton,
            null
    );
  }

  @Nonnull
  private static Button createNewButton(@Nonnull final Injector injector,
                                        @Nonnull final String url) {
    final var createButton = createButton(injector);
    createButton.addClickListener(event -> UI.getCurrent().navigate(url.toLowerCase()));
    return createButton;
  }

  @Nonnull
  private static Button createNewButton(@Nonnull final Injector injector,
                                        @Nonnull final Runnable onCreateAction) {
    final var createButton = createButton(injector);
    createButton.addClickListener(event -> onCreateAction.run());
    return createButton;
  }

  @Nonnull
  private static VAButton createButton(@Nonnull final Injector injector) {
    final var translation = injector.getInstance(TranslationHelper.class).getTranslation("common.new");
    return new VAButton(translation, VaadinIcon.PLUS.create());
  }

  private static void applyUserBadgeToToolbar(@Nonnull final Injector injector,
                                              @Nonnull final ViewToolbar toolbar) {
    final var currentUser = injector.getUserProperties().getCurrentUser(); //TODO user properties rpc call
    final var ui = UI.getCurrent();

    final var userBadge = new UserActionBadge(currentUser.getUsername())
//            .addItem(VaadinIcon.USER, "Profil", () -> ui.navigate(Routes.PROFILE))
            .addItemWithTranslationKey(VaadinIcon.SLIDER, "setting.user_settings", () -> ui.navigate(Routes.USER_SETTINGS))
            .addItemWithTranslationKey(VaadinIcon.SIGN_OUT, "shared.logout", () -> globalInterface.getUserProperties().logout());

    toolbar.addUserAction(userBadge);
  }

  @Nullable
  private static User getCurrentUser() {
    final var authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.getPrincipal() instanceof User) {
      return (User) authentication.getPrincipal();
    }

    return null;
  }

}
