package ch.verno.ui.base.settings.grid;

import ch.verno.ui.base.components.form.FormMode;
import ch.verno.ui.base.pages.detail.BaseDetailView;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.data.binder.Binder;
import jakarta.annotation.Nonnull;

public abstract class BaseSettingDetail<T> extends BaseDetailView<T> {

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
    spacer.getStyle().set("flex-grow", "1");

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