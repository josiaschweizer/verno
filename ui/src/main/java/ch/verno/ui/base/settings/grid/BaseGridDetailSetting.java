package ch.verno.ui.base.settings.grid;

import ch.verno.common.db.dto.base.BaseDto;
import ch.verno.common.gate.GlobalInterface;
import ch.verno.ui.base.settings.VABaseSetting;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.ItemDoubleClickEvent;
import com.vaadin.flow.component.icon.VaadinIcon;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public abstract class BaseGridDetailSetting<T extends BaseDto, G extends BaseSettingGrid<T>, D extends BaseSettingDetail<T>> extends VABaseSetting<T> {

  @Nonnull protected final G grid;
  @Nonnull protected final D detailView;

  protected BaseGridDetailSetting(@Nonnull final GlobalInterface globalInterface,
                                  @Nonnull final String titleKey,
                                  @Nonnull final G grid,
                                  @Nonnull final D detailView) {
    super(globalInterface, titleKey, false);
    setCardDefaultHeight();

    this.grid = grid;
    this.detailView = detailView;

    initializeComponents();
  }

  private void initializeComponents() {
    grid.initUI();
    grid.addItemDoubleClickListener(this::onGridItemDoubleClick);
    detailView.setAfterSave(this::displayGrid);

    setActionButton(getAddButton());
  }

  @Nonnull
  private Button getAddButton() {
    final var button = new Button(getAddButtonText(), VaadinIcon.PLUS.create());
    button.addClickListener(clickEvent -> displayDetail(null));
    return button;
  }

  private void displayDetail(@Nullable final Long entityId) {
    setContent(detailView);
    setActionButton(createBackToGridButton());
    detailView.setParameter(null, entityId);
  }

  @Nonnull
  private Button createBackToGridButton() {
    final var button = new Button(getBackButtonText(), VaadinIcon.ARROW_BACKWARD.create());
    button.addClickListener(event -> displayGrid());
    return button;
  }

  private void displayGrid() {
    setContent(grid);
    grid.refresh();
    setActionButton(getAddButton());
  }

  private void onGridItemDoubleClick(@Nonnull final ItemDoubleClickEvent<T> event) {
    displayDetail(getEntityId(event.getItem()));
  }

  @Nonnull
  @Override
  protected Component createContent() {
    return grid;
  }

  @Nonnull
  protected abstract String getAddButtonText();

  @Nonnull
  protected abstract String getBackButtonText();

  @Nullable
  protected abstract Long getEntityId(@Nonnull final T entity);
}