package ch.verno.ui.base.components.button.variants;

import ch.verno.ui.base.components.button.VAButton;
import ch.verno.ui.lib.icon.CustomIcons;
import ch.verno.ui.lib.icon.IconUtil;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import jakarta.annotation.Nonnull;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.BooleanSupplier;

public class VASaveButton extends VAButton {

  public static final String SAVE_KEY = "common.save";

  @Nullable private BooleanSupplier dirtyProvider;

  public VASaveButton(@Nullable final ComponentEventListener<ClickEvent<Button>> listener) {
    super();

    setText(getTranslation(SAVE_KEY));
    setIcon(IconUtil.creatExtraSmall(CustomIcons.SAVE));
    Optional.ofNullable(listener).ifPresent(this::addClickListener);
  }

  public VASaveButton(@Nonnull final ComponentEventListener<ClickEvent<Button>> listener,
                      @Nonnull final BooleanSupplier dirtyProvider) {
    this(listener);
    setDirtyActionProvider(dirtyProvider);
  }

  public void setDirtyActionProvider(@Nonnull final BooleanSupplier dirtyProvider) {
    this.dirtyProvider = dirtyProvider;
    addClickListener(e -> refreshDirtyState());
    refreshDirtyState();
  }

  public void refreshDirtyState() {
    if (dirtyProvider != null) {
      setDirty(dirtyProvider.getAsBoolean());
    }
  }

  public void setDirty(final boolean dirty) {
    setIcon(dirty ?
            IconUtil.creatExtraSmall(CustomIcons.SAVE_FILLED) :
            IconUtil.creatExtraSmall(CustomIcons.SAVE)
    );
  }

  /**
   * Returns the dirty provider value - returns per default false if the dirty provider is null
   */
  public boolean isDirty() {
    if (dirtyProvider != null) {
      return dirtyProvider.getAsBoolean();
    }

    return false;
  }

}