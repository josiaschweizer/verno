package ch.verno.ui.base.components.multilanguagefield;

import ch.verno.lib.language.Language;
import ch.verno.publ.CssImportConstants;
import ch.verno.publ.Publ;
import ch.verno.publ.VernoUtility;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.Nonnull;
import org.jetbrains.annotations.NonNls;

import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.Map;

@CssImport(CssImportConstants.VA_MULTI_LANGUAGE_FIELD)
public class VAMultiLanguageField extends CustomField<Map<Language, String>> {

  @NonNls public static final String VA_MULTI_LANGUAGE_FIELD_CLASSNAME = "va-multi-language-field";
  public static final String BUTTON_CLASSNAME = "va-multi-language-field-button";
  public static final String FIELD_LANGUAGE_CLASSNAME = "va-multi-language-field-language";
  public static final String FIELD_ROW_CLASSNAME = "va-multi-language-field-row";

  @Nonnull private final Language mainLanguage;
  private final Map<Language, TextField> languageFields = new LinkedHashMap<>();

  @Nonnull private final TextField mainField = new TextField();
  @Nonnull private final Button editButton = new Button(VaadinIcon.GLOBE.create());
  @Nonnull private final Dialog dialog = new Dialog();

  public VAMultiLanguageField(@Nonnull final Language mainLanguage) {
    this.mainLanguage = mainLanguage;

    addClassName(VA_MULTI_LANGUAGE_FIELD_CLASSNAME);

    configureMainField();
    configureButton();
    configureDialog();

    final var layout = new HorizontalLayout(mainField, editButton);
    layout.setPadding(false);
    layout.setSpacing(true);
    layout.setWidthFull();
    layout.expand(mainField);

    add(layout);
  }

  private void configureMainField() {
    mainField.setWidthFull();

    mainField.addValueChangeListener(event -> {
      final var field = languageFields.get(mainLanguage);

      if (field != null && !field.getValue().equals(event.getValue())) {
        field.setValue(event.getValue());
      }

      updateValue();
    });
  }

  private void configureButton() {
    editButton.addClassName(BUTTON_CLASSNAME);
    editButton.addClickListener(event -> dialog.open());
  }

  private void configureDialog() {
    dialog.setHeaderTitle("Translations");
    dialog.setWidth(VernoUtility.SEVEN_HUNDRED_PX_REM);

    final var content = new VerticalLayout();
    content.setPadding(false);
    content.setSpacing(true);
    content.setWidthFull();

    for (final var language : Language.values()) {
      final var row = createLanguageRow(language);
      content.add(row);
    }

    final var closeButton = new Button("Close", event -> dialog.close());
    closeButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

    dialog.add(content);
    dialog.getFooter().add(closeButton);
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

  @Override
  protected Map<Language, String> generateModelValue() {
    final var result = new EnumMap<Language, String>(Language.class);

    for (final var entry : languageFields.entrySet()) {
      final var value = entry.getValue().getValue();

      if (value != null && !value.isBlank()) {
        result.put(entry.getKey(), value);
      }
    }

    return result;
  }

  @Override
  protected void setPresentationValue(@Nonnull final Map<Language, String> newPresentationValue) {
    for (final var language : Language.values()) {
      final var value = newPresentationValue.getOrDefault(language, Publ.EMPTY_STRING);

      final var field = languageFields.get(language);
      if (field != null) {
        field.setValue(value);
      }
    }

    mainField.setValue(newPresentationValue.getOrDefault(mainLanguage, Publ.EMPTY_STRING));
  }
}