package ch.verno.ui.base.components.anchor;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.AnchorTarget;
import com.vaadin.flow.component.html.AttachmentType;
import com.vaadin.flow.server.streams.DownloadHandler;
import jakarta.annotation.Nonnull;

public class VAAnchor extends Anchor {

  public VAAnchor() {
    super();
  }

  public VAAnchor(@Nonnull final String text) {
    super(text);
  }

  public VAAnchor(@Nonnull final String href,
                  @Nonnull final String text) {
    super(href, text);
  }

  public VAAnchor(@Nonnull final String href,
                  @Nonnull final String text,
                  @Nonnull final AnchorTarget anchorTarget) {
    super(href, text, anchorTarget);
  }

  public VAAnchor(@Nonnull final String text,
                  @Nonnull final Component... components) {
    super(text, components);
  }

  public VAAnchor(@Nonnull final DownloadHandler downloadHandler,
                  @Nonnull final String text) {
    super(downloadHandler, text);
  }

  public VAAnchor(@Nonnull final DownloadHandler downloadHandler,
                  @Nonnull final String text,
                  @Nonnull final AttachmentType attachmentType) {
    super(downloadHandler, attachmentType, text);
  }
}
