package ch.verno.ui.base.detail;

import ch.verno.common.util.Publ;
import ch.verno.common.util.VernoConstants;
import ch.verno.ui.base.components.form.FormMode;
import ch.verno.ui.base.components.toolbar.ViewToolbarFactory;
import ch.verno.ui.base.components.toolbar.ViewToolbarResult;
import ch.verno.ui.base.factory.EntryFactory;
import ch.verno.ui.lib.Routes;
import ch.verno.ui.verno.FieldFactory;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.i18n.I18NProvider;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.server.VaadinService;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public abstract class BaseDetailView<T> extends VerticalLayout implements HasUrlParameter<Long> {

  @Nonnull
  public FormMode formMode = getDefaultFormMode();

  @Nonnull
  private final Binder<T> binder;
  @Nonnull
  protected EntryFactory<T> entryFactory;
  @Nonnull
  protected FieldFactory<T> fieldFactory;
  @Nullable
  protected I18NProvider i18nProvider;

  @Nonnull
  protected Button saveButton;
  @Nonnull
  protected ViewToolbarResult viewToolbar;

  @Nullable
  protected VerticalLayout addOnLayout;

  public BaseDetailView() {
    this.saveButton = new Button(VernoConstants.SAVE);
    this.viewToolbar = createViewToolbar();
    this.binder = createBinder();
    this.entryFactory = new EntryFactory<>();
    this.i18nProvider = getI18NProvider();
    this.fieldFactory = new FieldFactory<>(entryFactory, i18nProvider);
  }

  @Nullable
  protected I18NProvider getI18NProvider() {
    final var service = VaadinService.getCurrent();
    if (service != null && service.getInstantiator() != null) {
      return service.getInstantiator().getI18NProvider();
    }
    return null;
  }

  protected void init() {
    setWidthFull();
    setHeightFull();
    setPadding(false);
    setSpacing(false);

    add(viewToolbar.toolbar());

    initUI();

    saveButton.addClickListener(event -> save());
    binder.addValueChangeListener(event -> updateSaveButtonState());
    binder.addStatusChangeListener(event -> updateSaveButtonState());

    add(createSaveButtonLayout());
    initAdditionalInfoUIBelowSaveButton();

    applyFormMode(getDefaultFormMode());
    updateSaveButtonState();
  }

  @Nonnull
  protected ViewToolbarResult createViewToolbar() {
    final var result = ViewToolbarFactory.createDetailToolbar(getDetailPageName(), getDetailRoute());

    if (result.createButton() != null) {
      result.createButton().addClickListener(this::onCreateButtonClick);
    }
    if (result.editButton() != null) {
      result.editButton().addClickListener(this::onEditButtonClick);
    }

    return result;
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

  protected void applyFormMode(@Nonnull final FormMode formMode) {
    this.formMode = formMode;

    final boolean saveVisible = (formMode == FormMode.CREATE || formMode == FormMode.EDIT);
    saveButton.setVisible(saveVisible);

    if (formMode == FormMode.CREATE) {
      saveButton.setText(VernoConstants.CREATE + getDetailPageName());

      if (viewToolbar.createButton() != null) {
        viewToolbar.createButton().setVisible(false);
      }
      setAddOnVisible(false);
    } else if (formMode == FormMode.EDIT) {
      saveButton.setText(VernoConstants.UPDATE + getDetailPageName());
      setAddOnVisible(true);
    } else {
      saveButton.setText(VernoConstants.SAVE);

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

  protected void save() {
    if (formMode == FormMode.CREATE) {
      createBean(binder.getBean());
    } else if (formMode == FormMode.EDIT) {
      updateBean(binder.getBean());
    } else {
      return;
    }

    UI.getCurrent().navigate(getBasePageRoute());
  }

  protected abstract void initUI();

  protected void initAdditionalInfoUIBelowSaveButton() {
    // Can be overridden by subclasses to add additional UI components
  }

  @Nonnull
  protected abstract String getDetailPageName();

  @Nonnull
  protected String getDetailRoute() {
    return Routes.createUrlFromUrlSegments(getDetailPageName() + Publ.S, Routes.DETAIL);
  }

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
  protected VerticalLayout createSaveButtonLayout() {
    final var saveLayout = new HorizontalLayout();
    saveLayout.setWidthFull();
    saveLayout.setJustifyContentMode(JustifyContentMode.END);
    saveLayout.add(saveButton);

    return new VerticalLayout(saveLayout);
  }

  @Override
  public void setParameter(@Nullable final BeforeEvent event,
                           @OptionalParameter @Nullable final Long parameter) {
    if (parameter == null) {
      binder.setBean(newBeanInstance());
      applyFormMode(FormMode.CREATE);
      updateSaveButtonState();
    } else {
      final var bean = getBeanById(parameter);
      binder.setBean(bean);
      applyFormMode(FormMode.EDIT);
      updateSaveButtonState();
    }
  }

  @Nonnull
  protected Binder<T> getBinder() {
    return binder;
  }
}
