package ch.verno.ui.base.components.anchorbutton;

import ch.verno.ui.base.components.button.VAButton;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public class VAAnchorButton extends VAButton {

  @Nonnull
  private final String href;

  private final boolean openInNewTab;

  public VAAnchorButton(@Nonnull final String text,
                        @Nonnull final String href) {
    this(null, text, href, true);
  }

  public VAAnchorButton(@Nonnull final String text,
                        @Nonnull final String href,
                        final boolean openInNewTab) {
    this(null, text, href, openInNewTab);
  }

  public VAAnchorButton(@Nonnull final Component icon,
                        @Nonnull final String href) {
    this(icon, null, href, true);
  }

  public VAAnchorButton(@Nonnull final Component icon,
                        @Nonnull final String href,
                        final boolean openInNewTab) {
    this(icon, null, href, openInNewTab);
  }

  public VAAnchorButton(@Nonnull final Component icon,
                        @Nonnull final String text,
                        @Nonnull final String href) {
    this(icon, text, href, true);
  }

  public VAAnchorButton(@Nullable final Component icon,
                        @Nullable final String text,
                        @Nonnull final String href,
                        final boolean openInNewTab) {
    super();

    if (href.isBlank()) {
      throw new IllegalArgumentException("href must not be blank");
    }

    if (icon == null && (text == null || text.isBlank())) {
      throw new IllegalArgumentException("Either icon or text must be provided");
    }

    this.href = href;
    this.openInNewTab = openInNewTab;

    if (icon != null) {
      setIcon(icon);
    }

    if (text != null && !text.isBlank()) {
      setText(text);
    }

    addClickListener(event -> openHref());
  }

  private void openHref() {
    if (!isEnabled()) {
      return;
    }

    final UI ui = UI.getCurrent();
    if (ui == null) {
      return;
    }

    if (openInNewTab) {
      ui.getPage().open(href, "_blank");
    } else {
      ui.getPage().setLocation(href);
    }
  }

  @Nonnull
  public String getHref() {
    return href;
  }

  public boolean isOpenInNewTab() {
    return openInNewTab;
  }
}