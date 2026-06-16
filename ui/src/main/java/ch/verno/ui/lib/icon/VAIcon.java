package ch.verno.ui.lib.icon;

import ch.verno.lib.annotation.RestrictedTo;
import ch.verno.publ.Publ;
import ch.verno.publ.VernoUtility;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.dom.Style;
import jakarta.annotation.Nonnull;
import org.jetbrains.annotations.NonNls;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class VAIcon extends Span {

  private static final Logger LOGGER = LoggerFactory.getLogger(VAIcon.class);

  @NonNls private static final String RESOURCE_PREFIX = "META-INF/resources/";
  @NonNls private static final String DEFAULT_COLOR = "var(--lumo-contrast-60pct)";
  @NonNls public static final String CONFIGURE_SVG_ELEMENT_JS = """
          const svg = this.querySelector('svg');
          if (svg) {
            svg.style.width = $0;
            svg.style.height = $0;
            svg.style.display = 'block';
          }
          """;

  @RestrictedTo(IconUtil.class)
  protected VAIcon(@Nonnull final String path, @Nonnull final String size) {
    initializeIcon(path, size);
  }

  @RestrictedTo(IconUtil.class)
  protected VAIcon(@Nonnull final VaadinIcon icon, @Nonnull final String size) {
    initializeIcon(icon.create(), size);
  }

  private void initializeIcon(@Nonnull final String path, @Nonnull final String size) {
    try {
      String iconContent;
      if (path.endsWith(Publ.PNG)) {
        iconContent = loadPngContent(path);
      } else {
        iconContent = loadSvgContent(path);
      }
      getElement().setProperty("innerHTML", iconContent);

      configureStyling(size);
      configureSvgElement(size);
    } catch (IOException e) {
      LOGGER.error("Failed to load icon from path: {}", path, e);
      showErrorIcon();
    }
  }

  private void initializeIcon(@Nonnull final Icon icon, @Nonnull final String size) {
    removeAll();
    add(icon);

    configureStyling(size);
    icon.getStyle()
            .setWidth(size)
            .setHeight(size)
            .setDisplay(Style.Display.BLOCK);

    icon.setColor(DEFAULT_COLOR);
  }

  private void configureStyling(@Nonnull final String size) {
    getStyle().setDisplay(Style.Display.INLINE_FLEX)
            .setAlignItems(Style.AlignItems.CENTER)
            .setJustifyContent(Style.JustifyContent.CENTER)
            .setWidth(size)
            .setHeight(size)
            .setColor(DEFAULT_COLOR);
  }

  private void configureSvgElement(@Nonnull final String size) {
    getElement().executeJs(CONFIGURE_SVG_ELEMENT_JS, size);
  }

  @Nonnull
  private String loadSvgContent(@Nonnull final String path) throws IOException {
    final var resourcePath = RESOURCE_PREFIX + path;

    try (InputStream is = getClass().getClassLoader().getResourceAsStream(resourcePath)) {
      if (is == null) {
        throw new IOException("SVG resource not found: " + resourcePath);
      }
      return new String(is.readAllBytes(), StandardCharsets.UTF_8);
    }
  }

  @Nonnull
  private String loadPngContent(@Nonnull final String path) throws IOException {
    final var resourcePath = RESOURCE_PREFIX + path;

    try (final var inputStream = getClass().getClassLoader().getResourceAsStream(resourcePath)) {
      if (inputStream == null) {
        throw new IOException("PNG resource not found: " + resourcePath);
      }

      byte[] imageBytes = inputStream.readAllBytes();
      String base64 = java.util.Base64.getEncoder().encodeToString(imageBytes);

      return "<img src=\"data:image/png;base64," + base64 + "\" />";
    }
  }

  private void showErrorIcon() {
    setText("⚠");
    getStyle()
            .setColor(VernoUtility.LUMO_ERROR_COLOR)
            .setFontSize("1.2em");
  }

  public void setSize(@Nonnull final String size) {
    configureStyling(size);
    configureSvgElement(size);
  }

  public void setColor(@Nonnull final String color) {
    getStyle().setColor(color);
  }
}