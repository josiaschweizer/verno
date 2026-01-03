package ch.verno.ui.base.components.filter;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.function.SerializableConsumer;
import com.vaadin.flow.function.SerializableRunnable;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

@CssImport("./components/filter/va-filter-bar.css")
public class VAFilterBar extends VerticalLayout {

  @Nonnull
  private final VASearchFilter searchFilter;

  @Nonnull
  private final Button toggleFiltersButton;

  @Nonnull
  private final HorizontalLayout advancedFiltersRow;

  @Nullable
  private SerializableConsumer<String> searchHandler;

  @Nullable
  private SerializableRunnable onFiltersChanged;

  private boolean filtersVisible = false;

  public VAFilterBar() {
    setPadding(false);
    setSpacing(false);
    getStyle().setGap("0");
    setWidthFull();

    searchFilter = new VASearchFilter(null, getTranslation("base.search"));
    searchFilter.setWidthFull();

    toggleFiltersButton = new Button(getTranslation("base.filter"), VaadinIcon.FILTER.create());
    toggleFiltersButton.addClassName("va-toggle-filters");
    toggleFiltersButton.addClickListener(e -> setFiltersVisible(!filtersVisible));
    toggleFiltersButton.setVisible(false);

    final var topBar = new HorizontalLayout(searchFilter, toggleFiltersButton);
    topBar.setWidthFull();
    topBar.setPadding(false);
    topBar.setSpacing(true);
    topBar.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.END);

    topBar.setFlexGrow(1, searchFilter);
    toggleFiltersButton.addClassNames(LumoUtility.Margin.Right.SMALL);

    advancedFiltersRow = new HorizontalLayout();
    advancedFiltersRow.setWidthFull();
    advancedFiltersRow.setPadding(false);
    advancedFiltersRow.setSpacing(true);
    advancedFiltersRow.setVisible(false);

    addClassName(LumoUtility.Gap.MEDIUM);

    add(topBar, advancedFiltersRow);

    searchFilter.addValueChangeListener(e -> {
      if (searchHandler != null) {
        searchHandler.accept(e.getValue());
      }
      if (onFiltersChanged != null) {
        onFiltersChanged.run();
      }
    });
  }

  public void setSearchHandler(@Nullable SerializableConsumer<String> searchHandler) {
    this.searchHandler = searchHandler;
  }

  public void setOnFiltersChanged(@Nullable SerializableRunnable onFiltersChanged) {
    this.onFiltersChanged = onFiltersChanged;
  }

  public void addFilterComponent(@Nonnull final MultiSelectComboBox<Long> comboBox) {
    advancedFiltersRow.add(comboBox);
    toggleFiltersButton.setVisible(true);

    comboBox.addValueChangeListener(e -> {
      if (onFiltersChanged != null) {
        onFiltersChanged.run();
      }
    });
  }

  public void clearFilterComponents() {
    advancedFiltersRow.removeAll();
  }

  public void setFiltersVisible(boolean visible) {
    this.filtersVisible = visible;
    advancedFiltersRow.setVisible(visible);

    toggleFiltersButton.setIcon(visible ? VaadinIcon.ANGLE_UP.create() : VaadinIcon.FILTER.create());
  }

  public boolean isFiltersVisible() {
    return filtersVisible;
  }

  @Nonnull
  public String getSearchValue() {
    return searchFilter.getValue();
  }

  public void setSearchValue(@Nonnull String value) {
    searchFilter.setValue(value);
  }
}