package ch.verno.ui.base.settings;

import ch.verno.common.db.dto.base.BaseDto;
import ch.verno.ui.verno.settings.SettingEntryFactory;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.data.binder.Binder;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

@CssImport("./components/va-base-setting.css")
public abstract class VABaseSetting<T extends BaseDto> extends Div {

  @Nonnull
  private final Div headerWrapper;
  @Nonnull
  private final Div contentWrapper;
  @Nullable
  protected Component contentComponent;
  @Nullable
  private Span actionButtonSpan;

  @Nonnull
  protected final Binder<T> binder;
  @Nonnull
  protected T dto;

  @Nonnull
  public final SettingEntryFactory<T> settingEntryFactory;

  protected VABaseSetting(@Nonnull final String title) {
    settingEntryFactory = new SettingEntryFactory<>();
    dto = createNewBeanInstance();
    binder = createBinder();

    addClassName("setting-card");

    headerWrapper = new Div();
    headerWrapper.addClassName("setting-card-header");

    final var titleSpan = new Span(title);
    titleSpan.addClassName("setting-card-title");

    headerWrapper.add(titleSpan);
    add(headerWrapper);

    contentWrapper = new Div();
    add(contentWrapper);
  }

  protected final void setActionButton(@Nonnull final Button actionButton) {
    if (actionButtonSpan != null) {
      headerWrapper.remove(actionButtonSpan);
      actionButtonSpan = null;
    }

    actionButtonSpan = new Span(actionButton);
    actionButtonSpan.addClassName("setting-card-action-button");
    headerWrapper.add(actionButtonSpan);
  }

  @Override
  protected void onAttach(@Nonnull final AttachEvent attachEvent) {
    super.onAttach(attachEvent);

    if (contentComponent == null) {
      setContent(createContent());
    }
  }

  protected final void setContent(@Nonnull final Component newContent) {
    if (contentComponent != null) {
      contentWrapper.remove(contentComponent);
    }

    contentComponent = newContent;
    contentWrapper.add(contentComponent);
  }

  public void setCardDefaultHeight() {
    contentWrapper.addClassName("setting-card-content");
  }

  @Nonnull
  protected abstract Component createContent();

  @Nonnull
  protected abstract Binder<T> createBinder();

  @Nonnull
  protected abstract T createNewBeanInstance();
}