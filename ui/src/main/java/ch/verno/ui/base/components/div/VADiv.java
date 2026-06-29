package ch.verno.ui.base.components.div;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import jakarta.annotation.Nonnull;

public class VADiv extends Div {

  public VADiv(){
    super();
  }

  public VADiv(@Nonnull final Component... components){
    super(components);
  }

  public VADiv(@Nonnull final String text){
    super(text);
  }

}
