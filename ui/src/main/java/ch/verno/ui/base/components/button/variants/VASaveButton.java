package ch.verno.ui.base.components.button.variants;

import ch.verno.ui.base.components.button.VAButton;
import ch.verno.ui.lib.icon.CustomIcons;
import ch.verno.ui.lib.icon.IconUtil;

import javax.annotation.Nullable;
import java.util.function.BooleanSupplier;

public class VASaveButton extends VAButton {

  public static final String SAVE_KEY = "common.save";

  @Nullable private final BooleanSupplier dirtyProvider;

  public VASaveButton(@Nullable final BooleanSupplier dirtyProvider) {
    super();
    this.dirtyProvider = dirtyProvider;

    setContent(getTranslation(SAVE_KEY), IconUtil.creatExtraSmall(CustomIcons.SAVE));
    addClickListener(e -> refreshDirtyState());
    refreshDirtyState();
  }

  public void refreshDirtyState() {
    setDirty(dirtyProvider.getAsBoolean());
  }

  public void setDirty(final boolean dirty) {
    if (dirty) {
      setContent(getTranslation(SAVE_KEY), IconUtil.creatExtraSmall(CustomIcons.SAVE_FILLED));
    } else {
      setContent(getTranslation(SAVE_KEY), IconUtil.creatExtraSmall(CustomIcons.SAVE));
    }
  }

  public boolean isDirty() {
    return dirtyProvider.getAsBoolean();
  }

}
