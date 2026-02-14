package ch.verno.ui.base.components.file;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.IFrame;
import jakarta.annotation.Nonnull;

public abstract class AbstractFilePreview extends Div {

  @Nonnull private final FileType fileType;
  @Nonnull private final IFrame frame;


  public AbstractFilePreview(@Nonnull final FileType fileType) {
    this.fileType = fileType;
    this.frame = new IFrame();
  }

  protected void init() {
    frame.setSizeFull();
    frame.setWidth("100%");
    frame.setHeight("100%");

    frame.getElement().setAttribute("type", fileType.getMimeType());
    frame.getElement().setAttribute("allow", "fullscreen");
    frame.getElement().setAttribute("frameborder", "0");

    add(frame);
  }

  public void setSrc(@Nonnull final String src) {
    frame.setSrc(src);
  }

  public void clear() {
    frame.setSrc("about:blank");
  }

  public void applyDefaultStyle() {
    setSizeFull();
    setMinHeight("600px");
  }

}
