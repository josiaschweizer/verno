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

@CssImport("./components/setting/va-base-setting.css")
public abstract class VABaseSetting<T extends BaseDto> extends Div {

  @Nonnull
  protected final Binder<T> binder;
  @Nonnull
  protected T dto;

  @Nonnull
  private final Div headerWrapper;
  @Nonnull
  private final Div contentWrapper;
  @Nullable
  protected Component contentComponent;
  @Nullable
  private Span actionButtonSpan;
  @Nonnull
  protected Button saveButton;

  @Nonnull
  public final SettingEntryFactory<T> settingEntryFactory;

  protected VABaseSetting(@Nonnull final String titleKey,
                          final boolean showSaveButton) {
    settingEntryFactory = new SettingEntryFactory<>();
    dto = createNewBeanInstance();
    binder = createBinder();

    addClassName("setting-card");

    headerWrapper = new Div();
    headerWrapper.addClassName("setting-card-header");

    final var titleSpan = new Span(getTranslation(titleKey));
    titleSpan.addClassName("setting-card-title");

    headerWrapper.add(titleSpan);
    add(headerWrapper);

    contentWrapper = new Div();
    add(contentWrapper);

    createSaveButton(showSaveButton);
  }

  private void createSaveButton(final boolean showSaveButton) {
    saveButton = new Button(getTranslation(getTranslation("save")), e -> save());
    saveButton.setEnabled(false);

    if (showSaveButton) {
      setActionButton(saveButton);
    }
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

      binder.readBean(dto);
      binder.addStatusChangeListener(e -> saveButton.setEnabled(binder.hasChanges() && binder.isValid()));
      binder.addValueChangeListener(e -> saveButton.setEnabled(binder.hasChanges() && binder.isValid()));
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

  protected void save() {
    // Default implementation does nothing
  }
}