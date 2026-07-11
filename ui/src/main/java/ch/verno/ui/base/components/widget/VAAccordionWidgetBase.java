package ch.verno.ui.base.components.widget;

import ch.verno.lib.CssImportConstants;
import ch.verno.ui.base.components.button.VAButton;
import ch.verno.ui.base.components.has.sessionstorage.HasSessionStorage;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.accordion.AccordionPanel;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.Nonnull;

@CssImport(CssImportConstants.ACCORDION_WIDGET_BASE)
public abstract class VAAccordionWidgetBase extends AccordionPanel implements HasSessionStorage {

  protected VAAccordionWidgetBase() {
    setWidthFull();
  }

  protected final void buildUI() {
    removeAll();

    initSummary();
    initContent();
    loadOpenState();
    registerOpenStateListener();
  }

  private void initSummary() {
    final var header = new HorizontalLayout();
    header.addClassName("va-acc-widget__header");
    header.setAlignItems(FlexComponent.Alignment.CENTER);
    header.setPadding(false);
    header.setSpacing(true);
    header.setWidthFull();

    final var title = new Span(getTitleText());
    title.addClassNames("va-acc-widget__title", LumoUtility.FontWeight.SEMIBOLD);

    header.add(title);
    buildHeaderActions(header);

    header.setFlexGrow(1, title);
    setSummary(header);
  }

  @Nonnull
  protected abstract String getTitleText();

  protected void buildHeaderActions(@Nonnull final HorizontalLayout header) {
    // can be overridden by subclasses
  }

  protected abstract void initContent();

  protected void refresh() {
    // can be overridden by subclasses
  }

  @Nonnull
  @Override
  public Element getStorageElement() {
    return getElement();
  }

  @Nonnull
  protected String getSessionStorageKey() {
    return this.getId().orElse(getTitleText());
  }

  protected void loadOpenState() {
    loadFromSessionStorage(getSessionStorageKey(), value -> {
      if (Boolean.parseBoolean(value)) {
        setOpened(true);
      }
    });
  }

  protected void saveOpenState() {
    saveToSessionStorage(getSessionStorageKey(), String.valueOf(isOpened()));
  }

  private void registerOpenStateListener() {
    getElement().addPropertyChangeListener("opened", event -> saveOpenState());
  }

  @Nonnull
  protected final VAButton createHeaderButton(@Nonnull final String text,
                                              @Nonnull final VaadinIcon icon,
                                              @Nonnull final ComponentEventListener<ClickEvent<Button>> listener) {
    final var button = new VAButton(text, icon.create());
    button.addClassName("va-acc-widget__header-button");

    button.getElement()
            .addEventListener("click", e -> {
            })
            .addEventData("event.stopPropagation()");

    button.addClickListener(listener);
    return button;
  }

  @Nonnull
  protected final Component createHeaderSpacer() {
    final var spacer = new Span();
    spacer.addClassName("va-acc-widget__spacer");
    return spacer;
  }
}