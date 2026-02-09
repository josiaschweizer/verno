package ch.verno.ui.base.pages.detail;

import ch.verno.common.gate.GlobalInterface;
import ch.verno.ui.base.components.badge.VABadgeLabel;
import ch.verno.ui.base.components.form.FormMode;
import ch.verno.ui.base.components.toolbar.ViewToolbarFactory;
import ch.verno.ui.base.components.toolbar.ViewToolbarResult;
import ch.verno.ui.base.factory.EntryFactory;
import ch.verno.ui.verno.FieldFactory;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.List;

public abstract class BaseDetailView<T> extends VerticalLayout implements HasUrlParameter<Long> {

  @Nonnull private final GlobalInterface globalInterface;

  @Nonnull private final Binder<T> binder;
  @Nonnull protected EntryFactory<T> entryFactory;
  @Nonnull protected FieldFactory<T> fieldFactory;

  @Nonnull protected Button saveButton;
  @Nonnull protected Runnable afterSave;

  @Nullable protected ViewToolbarResult viewToolbar;
  @Nullable protected VerticalLayout addOnLayout;

  protected boolean showHeaderToolbar;
  protected boolean showPaddingAroundDetail;

  @Nullable protected FormMode pendingFormMode;
  @Nullable protected VABadgeLabel infoLabel;

  @Nonnull public FormMode formMode;

  protected BaseDetailView(@Nonnull final GlobalInterface globalInterface) {
    this(globalInterface, true);
  }

  protected BaseDetailView(@Nonnull final GlobalInterface globalInterface,
                           final boolean showHeaderToolbar) {
    this.globalInterface = globalInterface;
    this.showHeaderToolbar = showHeaderToolbar;

    this.saveButton = new Button(getTranslation("common.save"));
    this.saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    this.afterSave = () -> UI.getCurrent().navigate(getBasePageRoute());

    this.formMode = getDefaultFormMode();
    this.binder = createBinder();
    this.entryFactory = new EntryFactory<>(globalInterface.getI18NProvider());
    this.fieldFactory = new FieldFactory<>(entryFactory, globalInterface.getI18NProvider());
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

    saveButton.addClickListener(event -> save());
    binder.addValueChangeListener(event -> updateSaveButtonState());
    binder.addStatusChangeListener(event -> updateSaveButtonState());

    add(createActionButtonLayout());
    initAdditionalInfoUIBelowSaveButton();

    applyFormMode(resolveInitialFormMode());
    updateSaveButtonState();
  }

  @Nonnull
  protected ViewToolbarResult createViewToolbar() {
    final var result = ViewToolbarFactory.createDetailToolbar(globalInterface, getDetailPageName(), getDetailRoute());

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
      return;
    }

    final var bean = binder.getBean();
    if (bean == null) {
      saveButton.setEnabled(false);
      return;
    }

    final boolean valid = binder.isValid();
    saveButton.setEnabled(valid);
  }

  public void applyFormMode(@Nonnull final FormMode formMode) {
    this.formMode = formMode;

    final boolean saveVisible = (formMode == FormMode.CREATE || formMode == FormMode.EDIT);
    saveButton.setVisible(saveVisible);

    if (formMode == FormMode.CREATE) {
      saveButton.setText(getTranslation("shared.create"));

      if (viewToolbar.createButton() != null) {
        viewToolbar.createButton().setVisible(false);
      }
      setAddOnVisible(false);
    } else if (formMode == FormMode.EDIT) {
      saveButton.setText(getTranslation("shared.update"));
      setAddOnVisible(true);
    } else {
      saveButton.setText(getTranslation("common.save"));

      if (viewToolbar.createButton() != null) {
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

    afterSave.run();
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

  @Nonnull
  protected abstract T createBean(@Nonnull final T bean);

  @Nonnull
  protected abstract T updateBean(@Nonnull final T bean);

  @Nonnull
  protected abstract T newBeanInstance();

  protected abstract T getBeanById(@Nonnull final Long id);

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
  public HorizontalLayout createLayoutFromComponents(@Nonnull final Component... components) {
    final var layout = new HorizontalLayout();
    layout.setWidthFull();

    layout.getStyle().set("flex-wrap", "wrap");
    layout.setDefaultVerticalComponentAlignment(Alignment.START);

    for (final var component : components) {
      component.getElement().getStyle().set("min-width", "260px");
      component.getElement().getStyle().set("flex", "1 1 260px");
      layout.add(component);
    }

    return layout;
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
      pendingFormMode = forcedFormMode != null ? forcedFormMode : FormMode.CREATE;
      updateSaveButtonState();
      return;
    }

    final var bean = getBeanById(parameter);
    binder.setBean(bean);

    if (forcedFormMode != null) {
      pendingFormMode = forcedFormMode;
    } else {
      pendingFormMode = getFormModeByBean(bean);
    }

    updateSaveButtonState();
  }

  @Nullable
  private FormMode parseForcedMode(@Nullable final BeforeEvent event) {
    if (event == null) {
      return null;
    }

    final var params = event.getLocation().getQueryParameters().getParameters();
    final List<String> values = params.getOrDefault("mode", List.of());
    if (values.isEmpty()) {
      return null;
    }

    final var raw = values.getFirst();
    if (raw == null) {
      return null;
    }

    if ("view".equalsIgnoreCase(raw)) {
      return FormMode.VIEW;
    }
    if ("edit".equalsIgnoreCase(raw)) {
      return FormMode.EDIT;
    }
    if ("create".equalsIgnoreCase(raw)) {
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
}