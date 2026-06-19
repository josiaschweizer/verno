package ch.verno.ui.base.components.multilanguagefield;

import ch.verno.lib.New;
import ch.verno.lib.lib.language.Language;
import ch.verno.lib.CssImportConstants;
import ch.verno.lib.Publ;
import ch.verno.publ.VernoUtility;
import ch.verno.ui.base.components.button.ButtonBuilder;
import ch.verno.ui.base.components.button.VAButton;
import ch.verno.ui.base.components.dialog.VADialog;
import ch.verno.ui.base.components.entry.textfield.VATextField;
import ch.verno.ui.lib.icon.IconUtil;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.Nonnull;
import org.jetbrains.annotations.NonNls;

import javax.annotation.Nullable;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@CssImport(CssImportConstants.VA_MULTI_LANGUAGE_FIELD)
public class VAMultiLanguageField extends CustomField<Map<Language, String>> {

  @NonNls public static final String VA_MULTI_LANGUAGE_FIELD_CLASSNAME = "va-multi-language-field";
  @NonNls public static final String BUTTON_CLASSNAME = "va-multi-language-field-button";
  @NonNls public static final String FIELD_LANGUAGE_CLASSNAME = "va-multi-language-field-language";
  @NonNls public static final String FIELD_ROW_CLASSNAME = "va-multi-language-field-row";

  @Nonnull private final Language mainLanguage;
  @Nonnull private final List<Language> configuredLanguages;
  @Nonnull private final Map<Language, TextField> languageFields;

  @Nonnull private final VATextField mainField;
  @Nonnull private final VADialog dialog;

  @Nullable private Map<Language, String> oldPresentationValue;

  public VAMultiLanguageField(@Nonnull final Language mainLanguage,
                              @Nonnull final List<Language> configuredLanguages) {
    this.mainLanguage = mainLanguage;
    this.configuredLanguages = configuredLanguages;
    this.languageFields = New.hashMap();

    addClassName(VA_MULTI_LANGUAGE_FIELD_CLASSNAME);

    this.mainField = configureMainField();
    this.dialog = configureDialog();
    final var editButton = configureButton();

    final var layout = new HorizontalLayout(mainField, editButton);
    layout.setPadding(false);
    layout.setSpacing(true);
    layout.setWidthFull();
    layout.expand(mainField);

    add(layout);
  }

  @Nonnull
  private VATextField configureMainField() {
    final var mainField = new VATextField();
    mainField.setWidthFull();
    mainField.addValueChangeListener(event -> {
      final var field = languageFields.get(mainLanguage);

      if (field != null && !field.getValue().equals(event.getValue())) {
        field.setValue(event.getValue());
      }

      updateValue();
    });
    return mainField;
  }

  @Nonnull
  private VAButton configureButton() {
    final var editButton = ButtonBuilder.iconOnly(IconUtil.create(VaadinIcon.GLOBE), "Edit");
    editButton.addClassName(BUTTON_CLASSNAME);
    editButton.addClickListener(event -> dialog.open());
    return editButton;
  }

  @Nonnull
  private VADialog configureDialog() {
    final var dialog = new VADialog();
    dialog.setHeaderTitle("Translations");
    dialog.setWidth(VernoUtility.SEVEN_HUNDRED_PX_REM);

    final var content = new VerticalLayout();
    content.setPadding(false);
    content.setSpacing(true);
    content.setWidthFull();

    for (final var language : configuredLanguages) {
      final var row = createLanguageRow(language);
      content.add(row);
    }

    final var saveButton = new VAButton("Close", e -> {
      this.oldPresentationValue = getValue();
      dialog.close();
    });
    saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    final var closeButton = new Button("Cancel", event -> { //TODO translate cancel
      if (oldPresentationValue != null) {
        setPresentationValue(oldPresentationValue);
      }
      dialog.close();
    });
    closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

    dialog.add(content);
    dialog.getFooter().add(closeButton, saveButton);
    return dialog;
  }

  @Nonnull
  private HorizontalLayout createLanguageRow(@Nonnull final Language language) {
    final var label = new Span(language.name());
    label.addClassNames(
            FIELD_LANGUAGE_CLASSNAME,
            LumoUtility.FontWeight.SEMIBOLD,
            LumoUtility.TextColor.SECONDARY
    );

    final var field = new TextField();
    field.setWidthFull();

    field.addValueChangeListener(event -> {
      if (language == mainLanguage && !mainField.getValue().equals(event.getValue())) {
        mainField.setValue(event.getValue());
      }

      updateValue();
    });

    languageFields.put(language, field);

    final var rowField = new HorizontalLayout(label, field);
    rowField.addClassName(FIELD_ROW_CLASSNAME);
    rowField.setWidthFull();
    rowField.setAlignItems(HorizontalLayout.Alignment.CENTER);
    rowField.expand(field);

    return rowField;
  }

  @Nonnull
  @Override
  public Map<Language, String> getEmptyValue() {
    return new EnumMap<>(Language.class);
  }

  @Nonnull
  @Override
  protected Map<Language, String> generateModelValue() {
    final var result = new EnumMap<Language, String>(Language.class);

    for (final var entry : languageFields.entrySet()) {
      final var value = entry.getValue().getValue();

      if (value != null && !value.isBlank()) {
        result.put(entry.getKey(), value.trim());
      }
    }

    return result;
  }

  @Override
  protected void setPresentationValue(@Nullable final Map<Language, String> newPresentationValue) {
    final var value = newPresentationValue == null
            ? getEmptyValue()
            : new EnumMap<>(newPresentationValue);
    this.oldPresentationValue = new EnumMap<>(value);

    for (final var language : configuredLanguages) {
      final var field = languageFields.get(language);
      if (field != null) {
        field.setValue(value.getOrDefault(language, Publ.EMPTY_STRING));
      }
    }

    mainField.setValue(value.getOrDefault(mainLanguage, Publ.EMPTY_STRING));
  }
}