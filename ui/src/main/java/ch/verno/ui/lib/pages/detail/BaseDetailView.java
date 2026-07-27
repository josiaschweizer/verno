package ch.verno.ui.lib.pages.detail;

import ch.verno.ui.base.components.badge.VABadgeLabel;
import ch.verno.ui.base.components.button.variants.VASaveButton;
import ch.verno.ui.base.components.form.FormMode;
import ch.verno.ui.base.components.notification.NotificationFactory;
import ch.verno.ui.base.components.toolbar.ViewToolbarFactory;
import ch.verno.ui.base.components.toolbar.ViewToolbarResult;
import ch.verno.ui.base.factory.EntryFactory;
import ch.verno.ui.base.shortcut.DefaultVernoShortcuts;
import ch.verno.ui.base.shortcut.ShortcutRegistrationUtil;
import ch.verno.ui.base.shortcut.registry.ShortcutController;
import ch.verno.ui.verno.FieldFactory;
import com.google.inject.Injector;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.i18n.I18NProvider;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.jetbrains.annotations.NonNls;

import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public abstract class BaseDetailView<T> extends VerticalLayout implements HasUrlParameter<Long> {

  @NonNls public static final String MODE = "mode";
  @NonNls public static final String FORM_MODE_VIEW = "view";
  @NonNls public static final String FORM_MODE_EDIT = "edit";
  @NonNls public static final String FORM_MODE_CREATE = "create";

  @Nonnull protected final Injector injector;

  @Nonnull private final Binder<T> binder;
  @Nonnull protected EntryFactory<T> entryFactory;
  @Nonnull protected FieldFactory<T> fieldFactory;

  @Nonnull protected VASaveButton saveButton;
  @Nonnull protected Runnable afterSave;

  @Nullable protected ViewToolbarResult viewToolbar;
  @Nullable protected VerticalLayout addOnLayout;

  protected boolean showHeaderToolbar;
  protected boolean showPaddingAroundDetail;

  @Nullable protected VABadgeLabel infoLabel;

  @Nonnull public FormMode formMode;
  @Nullable protected FormMode pendingFormMode;

  protected BaseDetailView(@Nonnull final Injector injector) {
    this(injector, true);
  }

  protected BaseDetailView(@Nonnull final Injector injector,
                           final boolean showHeaderToolbar) {
    this.injector = injector;
    this.showHeaderToolbar = showHeaderToolbar;

    this.binder = createBinder();
    this.formMode = getDefaultFormMode();

    this.saveButton = createSaveButton();
    this.afterSave = () -> UI.getCurrent().navigate(getBasePageRoute());

    final var i18nProvider = injector.getInstance(I18NProvider.class);
    this.entryFactory = new EntryFactory<>(i18nProvider);
    this.fieldFactory = new FieldFactory<>(entryFactory, i18nProvider);
  }

  @Override
  protected void onAttach(final AttachEvent attachEvent) {
    super.onAttach(attachEvent);
    preInit();
    init();
  }

  protected void preInit() {
    this.viewToolbar = createViewToolbar();
  }

  protected void init() {
    setWidthFull();
    setHeightFull();
    setPadding(false);
    setSpacing(false);

    if (showHeaderToolbar && viewToolbar != null) {
      final var toolbar = viewToolbar.toolbar();
      add(toolbar);
    }

    initUI();

    binder.addValueChangeListener(event -> updateSaveButtonState());
    binder.addStatusChangeListener(event -> updateSaveButtonState());

    add(createActionButtonLayout());
    initAdditionalInfoUIBelowSaveButton();

    applyFormMode(resolveInitialFormMode());
    updateSaveButtonState();
  }

  @Nonnull
  protected ViewToolbarResult createViewToolbar() {
    final var result = ViewToolbarFactory.createDetailToolbar(injector, getDetailPageName(), getDetailRoute());

    if (result.createButton() != null) {
      result.createButton().addClickListener(this::onCreateButtonClick);
    }
    if (result.editButton() != null) {
      result.editButton().addClickListener(this::onEditButtonClick);
    }

    final var toolbar = result.toolbar();
    infoLabel = getInfoBadge();
    final var actionMenu = getToolbarContextMenu();
    if (infoLabel != null) {
      toolbar.addActionButton(infoLabel, true);
    }
    if (actionMenu != null) {
      toolbar.addAction(actionMenu);
    }

    return result;
  }

  @Nonnull
  private VASaveButton createSaveButton() {
    final var button = new VASaveButton(e -> save());
    button.setDirtyActionProvider(binder::hasChanges);
    button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

    final var registration = ShortcutRegistrationUtil.addClickShortcut(button, DefaultVernoShortcuts.SAVE);
    injector.getInstance(ShortcutController.class).register(
            DefaultVernoShortcuts.SAVE,
            button::click,
            this,
            registration
    );
    return button;
  }

  @Nullable
  protected VABadgeLabel getInfoBadge() {
    // can be overridden by subclasses to add an info component to the toolbar
    return null;
  }

  @Nullable
  protected Component getToolbarContextMenu() {
    // can be overridden by subclasses to add an action menu to the toolbar
    return null;
  }

  protected void updateSaveButtonState() {
    final boolean canSaveByMode = (formMode == FormMode.CREATE || formMode == FormMode.EDIT);
    if (!canSaveByMode) {
      saveButton.setEnabled(false);
    } else {
      final var bean = binder.getBean();
      if (bean == null) {
        saveButton.setEnabled(false);
        return;
      }

      final boolean valid = binder.isValid();
      saveButton.setEnabled(valid);
    }

    saveButton.refreshDirtyState();
  }

  public void applyFormMode(@Nonnull final FormMode formMode) {
    this.formMode = formMode;

    final boolean saveVisible = (formMode == FormMode.CREATE || formMode == FormMode.EDIT);
    saveButton.setVisible(saveVisible);

    if (formMode == FormMode.CREATE) {
      saveButton.setText(getTranslation("shared.create"));

      if (viewToolbar != null && viewToolbar.createButton() != null) {
        viewToolbar.createButton().setVisible(false);
      }

      setAddOnVisible(false);
    } else if (formMode == FormMode.EDIT) {
      saveButton.setText(getTranslation("shared.update"));
      setAddOnVisible(true);
    } else {
      saveButton.setText(getTranslation("common.save"));

      if (viewToolbar != null && viewToolbar.createButton() != null) {
        viewToolbar.createButton().setVisible(true);
      }

      setAddOnVisible(true);
    }

    binder.getFields().forEach(f -> f.setReadOnly(formMode == FormMode.VIEW));
  }

  private void setAddOnVisible(final boolean visible) {
    if (addOnLayout != null) {
      addOnLayout.setVisible(visible);
    }
  }

  protected void setShowHeaderToolbar(final boolean showHeaderToolbar) {
    this.showHeaderToolbar = showHeaderToolbar;
  }

  protected void setShowPaddingAroundDetail(final boolean showPaddingAroundDetail) {
    this.showPaddingAroundDetail = showPaddingAroundDetail;
  }

  protected void save() {
    if (formMode == FormMode.CREATE) {
      createBean(binder.getBean());
    } else if (formMode == FormMode.EDIT) {
      updateBean(binder.getBean());
    } else {
      return;
    }

    afterSave();
  }

  protected abstract void initUI();

  protected void initAdditionalInfoUIBelowSaveButton() {
    // Can be overridden by subclasses to add additional UI components
  }

  @Nonnull
  protected abstract String getDetailPageName();

  @Nonnull
  protected abstract String getDetailRoute();

  @Nonnull
  protected abstract String getBasePageRoute();

  @Nonnull
  protected FormMode getDefaultFormMode() {
    return FormMode.VIEW;
  }

  @Nonnull
  protected abstract Binder<T> createBinder();

  protected abstract void createBean(@Nonnull final T bean);

  protected abstract void updateBean(@Nonnull final T bean);

  @Nonnull
  protected abstract T newBeanInstance();

  @Nonnull
  protected abstract Optional<T> getBeanById(@Nonnull final Long id);

  protected void onCreateButtonClick(@Nonnull final ClickEvent<Button> event) {
    binder.setBean(newBeanInstance());
    applyFormMode(FormMode.CREATE);
    updateSaveButtonState();
  }

  protected void onEditButtonClick(@Nonnull final ClickEvent<Button> event) {
    applyFormMode(FormMode.EDIT);
    updateSaveButtonState();
  }

  @Nonnull
  protected VerticalLayout createActionButtonLayout() {
    final var cancel = new Button(getTranslation("shared.cancel"));
    cancel.addClickListener(event -> afterSave.run());

    final var saveLayout = new HorizontalLayout();
    saveLayout.setWidthFull();
    saveLayout.setJustifyContentMode(JustifyContentMode.END);
    saveLayout.add(cancel, saveButton);

    final var saveWrapperLayout = new VerticalLayout(saveLayout);
    saveWrapperLayout.setPadding(showPaddingAroundDetail);
    return saveWrapperLayout;
  }

  @Override
  public void setParameter(@Nullable final BeforeEvent event,
                           @OptionalParameter @Nullable final Long parameter) {
    final var forcedFormMode = parseForcedMode(event);

    if (parameter == null) {
      binder.setBean(newBeanInstance());
      pendingFormMode = Objects.requireNonNullElse(forcedFormMode, FormMode.CREATE);

      updateSaveButtonState();
    } else {
      final var bean = getBeanById(parameter).orElseGet(this::newBeanInstance);
      binder.setBean(bean);
      pendingFormMode = Objects.requireNonNullElseGet(forcedFormMode, () -> getFormModeByBean(bean));

      updateSaveButtonState();
    }

    applyFormMode(pendingFormMode);
  }

  @Nullable
  private FormMode parseForcedMode(@Nullable final BeforeEvent event) {
    if (event == null) {
      return null;
    }

    final var params = event.getLocation().getQueryParameters().getParameters();
    final List<String> values = params.getOrDefault(MODE, List.of());
    if (values.isEmpty()) {
      return null;
    }

    final var raw = values.getFirst();
    if (raw == null) {
      return null;
    }

    if (FORM_MODE_VIEW.equalsIgnoreCase(raw)) {
      return FormMode.VIEW;
    }
    if (FORM_MODE_EDIT.equalsIgnoreCase(raw)) {
      return FormMode.EDIT;
    }
    if (FORM_MODE_CREATE.equalsIgnoreCase(raw)) {
      return FormMode.CREATE;
    }

    return null;
  }

  @Nonnull
  protected FormMode resolveInitialFormMode() {
    return pendingFormMode != null ? pendingFormMode : getDefaultFormMode();
  }

  @Nonnull
  public FormMode getFormModeByBean(@Nonnull final T bean) {
    // Default to EDIT mode when a bean is loaded; can be customized by subclasses
    return FormMode.EDIT;
  }

  @Nonnull
  protected Binder<T> getBinder() {
    return binder;
  }

  public void setAfterSave(@Nonnull final Runnable afterSave) {
    this.afterSave = afterSave;
  }

  protected void afterSave() {
    NotificationFactory.showSuccessNotification(MessageFormat.format(getTranslation("shared.0.saved.successfully"), getDetailPageName()));
    afterSave.run();
  }
}