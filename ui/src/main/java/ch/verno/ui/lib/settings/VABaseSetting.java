package ch.verno.ui.lib.settings;

import ch.verno.contract.dto.table.base.BaseDto;
import ch.verno.contract.dto.table.setting.TenantSettingDto;
import ch.verno.lib.CssImportConstants;
import ch.verno.lib.Publ;
import ch.verno.ui.base.components.badge.VABadgeLabel;
import ch.verno.ui.base.components.button.VAButton;
import ch.verno.ui.base.factory.EntryFactory;
import ch.verno.ui.verno.settings.SettingEntryFactory;
import com.google.inject.Injector;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.i18n.I18NProvider;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.NonNls;

@CssImport(CssImportConstants.VA_BASE_SETTING)
public abstract class VABaseSetting<T extends BaseDto> extends Div {

  @NonNls public static final String SETTING_CARD_CONTENT_CLASSNAME = "setting-card-content";
  @NonNls public static final String SETTING_CARD_ACTION_BUTTON_CLASSNAME = "setting-card-action-button";
  @NonNls public static final String SETTING_CARD_CLASSNAME = "setting-card";
  @NonNls public static final String SETTING_CARD_HEADER_CLASSNAME = "setting-card-header";
  @NonNls public static final String SETTING_CARD_TITLE_CLASSNAME = "setting-card-title";

  @Nonnull public final Injector injector;

  @Nonnull protected T dto;
  @Nonnull protected final Binder<T> binder;

  @Nonnull private final Div headerWrapper;
  @Nullable private VABadgeLabel headerBadge;
  @Nullable private Span actionButtonSpan;
  @Nonnull protected VAButton saveButton;

  @Nonnull private final Div contentWrapper;
  @Nullable protected Component contentComponent;

  @Nonnull public final SettingEntryFactory<T> settingEntryFactory;
  @Nonnull protected final EntryFactory<TenantSettingDto> entryFactory;

  protected VABaseSetting(@Nonnull final Injector injector,
                          @Nonnull final String titleKey,
                          final boolean showSaveButton) {
    this.injector = injector;

    this.settingEntryFactory = new SettingEntryFactory<>();
    this.entryFactory = new EntryFactory<>(injector.getInstance(I18NProvider.class));

    this.dto = createNewBeanInstance();
    this.binder = createBinder();

    headerWrapper = new Div();
    headerWrapper.addClassName(SETTING_CARD_HEADER_CLASSNAME);

    final var titleSpan = new Span(getTranslation(titleKey));
    titleSpan.addClassName(SETTING_CARD_TITLE_CLASSNAME);

    headerWrapper.add(titleSpan);
    add(headerWrapper);

    contentWrapper = new Div();
    add(contentWrapper);

    addSaveButton(showSaveButton);
    addClassName(SETTING_CARD_CLASSNAME);
  }

  private void addSaveButton(final boolean showSaveButton) {
    saveButton = new VAButton(getTranslation("common.save"), e -> save());
    saveButton.setEnabled(false);

    if (showSaveButton) {
      addActionButtons(saveButton);
    }
  }

  protected final void addActionButtons(@Nonnull final Button... actionButtons) {
    if (actionButtonSpan != null) {
      headerWrapper.remove(actionButtonSpan);
      actionButtonSpan = null;
    }

    actionButtonSpan = new Span();
    actionButtonSpan.addClassName(SETTING_CARD_ACTION_BUTTON_CLASSNAME);

    for (final var actionButton : actionButtons) {
      actionButtonSpan.add(actionButton);
    }

    headerWrapper.add(actionButtonSpan);
  }

  protected final void setHeaderBadge(@Nullable final VABadgeLabel badge) {
    if (headerBadge != null) {
      headerWrapper.remove(headerBadge);
      headerBadge = null;
    }

    if (badge != null) {
      headerBadge = badge;
      headerWrapper.addComponentAtIndex(1, headerBadge);
    }
  }

  @Override
  protected void onAttach(@Nonnull final AttachEvent attachEvent) {
    super.onAttach(attachEvent);

    if (contentComponent == null) {
      setContent(createContent());

      binder.readBean(dto);
      binder.addStatusChangeListener(e -> binderStatusChanged());
      binder.addValueChangeListener(e -> binderValueChanged());
    }

    if (isAlwaysReadOnly()) {
      binder.setReadOnly(isAlwaysReadOnly());
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
    contentWrapper.addClassName(SETTING_CARD_CONTENT_CLASSNAME);
  }

  @Nonnull
  protected abstract Component createContent();

  @Nonnull
  protected abstract Class<T> getBeanType();

  @Nonnull
  protected abstract T createNewBeanInstance();

  protected void save() {
    throw new NotImplementedException("Save method not implemented");
  }

  @Nonnull
  private Binder<T> createBinder() {
    return new Binder<>(getBeanType());
  }

  protected void binderStatusChanged() {
    updateSaveButtonStatus(binder.hasChanges() && binder.isValid());
  }

  protected void binderValueChanged() {
    updateSaveButtonStatus(binder.hasChanges() && binder.isValid());
  }

  private void updateSaveButtonStatus(final boolean enabled) {
    if (!enabled) {
      saveButton.setTooltipText(getTranslation("common.you.have.to.enter.all.required.fields.to.save.your.config"));
      saveButton.setEnabled(false);
    } else {
      saveButton.setTooltipText(Publ.EMPTY_STRING);
      saveButton.setEnabled(true);
    }
  }

  protected boolean isAlwaysReadOnly() {
    // to be overridden if setting panel should be read only
    return false;
  }
}