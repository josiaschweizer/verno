package ch.verno.common.lib.i18n;

import ch.verno.common.gate.GlobalInterface;
import com.vaadin.flow.component.UI;
import jakarta.annotation.Nonnull;

public abstract class AbstractTranslationHelper {

  @Nonnull
  public String getTranslation(@Nonnull final GlobalInterface globalInterface,
                               @Nonnull final String key) {
    return TranslationHelper.getTranslation(globalInterface, key);
  }

}
