package ch.verno.ui.lib.settings.grid;

import ch.verno.lib.VernoUtility;
import ch.verno.ui.base.components.form.FormMode;
import ch.verno.ui.lib.pages.detail.BaseDetailView;
import com.google.inject.Injector;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.html.Div;
import jakarta.annotation.Nonnull;

public abstract class BaseSettingDetail<T> extends BaseDetailView<T> {

  public BaseSettingDetail(@Nonnull final Injector injector) {
    super(injector);
  }

  @Override
  protected void onAttach(final AttachEvent attachEvent) {
    // we have to override the attach so that init does not get called every time the detail view gets attached
    // and so the form data always grows (because of the add() calls in init)
  }

  @Override
  protected void init() {
    setWidthFull();
    setHeightFull();
    setPadding(false);
    setSpacing(false);

    initUI();

    this.saveButton.addClickListener(event -> save());
    getBinder().addValueChangeListener(event -> updateSaveButtonState());
    getBinder().addStatusChangeListener(event -> updateSaveButtonState());

    final var spacer = new Div();
    spacer.getStyle().setFlexGrow(VernoUtility.FLEX_GROW_ONE);

    add(spacer);
    add(createActionButtonLayout());

    applyFormMode(getDefaultFormMode());
    updateSaveButtonState();
  }

  @Override
  protected void save() {
    if (formMode == FormMode.CREATE) {
      createBean(getBinder().getBean());
    } else if (formMode == FormMode.EDIT) {
      updateBean(getBinder().getBean());
    } else {
      return;
    }

    afterSave.run();
  }

  @Nonnull
  @Override
  protected FormMode getDefaultFormMode() {
    return FormMode.EDIT;
  }
}