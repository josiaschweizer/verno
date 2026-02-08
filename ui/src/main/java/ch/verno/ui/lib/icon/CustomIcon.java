package ch.verno.ui.lib.icon;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.dom.Style;
import jakarta.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class CustomIcon extends Span {

  private static final Logger LOGGER = LoggerFactory.getLogger(CustomIcon.class);

  private static final String RESOURCE_PREFIX = "META-INF/resources/";
  private static final String DEFAULT_COLOR = "var(--lumo-contrast-60pct)";

  public CustomIcon(@Nonnull final String path, @Nonnull final String size) {
    initializeIcon(path, size);
  }

  private void initializeIcon(@Nonnull final String path, @Nonnull final String size) {
    try {
      final var svgContent = loadSvgContent(path);
      getElement().setProperty("innerHTML", svgContent);

      configureStyling(size);
      configureSvgElement(size);
    } catch (IOException e) {
      LOGGER.error("Failed to load icon from path: {}", path, e);
      showErrorIcon();
    }
  }

  private void configureStyling(@Nonnull final String size) {
    getStyle()
            .setDisplay(Style.Display.INLINE_FLEX)
            .setAlignItems(Style.AlignItems.CENTER)
            .setJustifyContent(Style.JustifyContent.CENTER)
            .setWidth(size)
            .setHeight(size)
            .setColor(DEFAULT_COLOR);
  }

  private void configureSvgElement(@Nonnull final String size) {
    getElement().executeJs(
            """
                    const svg = this.querySelector('svg');
                    if (svg) {
                      svg.style.width = $0;
                      svg.style.height = $0;
                      svg.style.display = 'block';
                    }
                    """,
            size
    );
  }

  @Nonnull
  private String loadSvgContent(@Nonnull final String path) throws IOException {
    final String resourcePath = RESOURCE_PREFIX + path;

    try (InputStream is = getClass().getClassLoader().getResourceAsStream(resourcePath)) {
      if (is == null) {
        throw new IOException("SVG resource not found: " + resourcePath);
      }
      return new String(is.readAllBytes(), StandardCharsets.UTF_8);
    }
  }

  private void showErrorIcon() {
    setText("⚠");
    getStyle()
            .setColor("var(--lumo-error-color)")
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