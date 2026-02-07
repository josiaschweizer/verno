package ch.verno.ui.base.factory;

import ch.verno.common.base.components.entry.phonenumber.PhoneNumber;
import ch.verno.common.db.dto.YearWeekDto;
import ch.verno.common.db.dto.table.GenderDto;
import ch.verno.common.lib.phonenumber.PhoneNumberFormatter;
import ch.verno.publ.Publ;
import ch.verno.ui.base.components.colorpicker.ColorPresets;
import ch.verno.ui.base.components.colorpicker.VAColorPicker;
import ch.verno.ui.base.components.entry.combobox.VAComboBox;
import ch.verno.ui.base.components.entry.numberfield.VANumberField;
import ch.verno.ui.base.components.entry.phonenumber.PhoneEntry;
import ch.verno.ui.base.components.entry.textfield.VATextField;
import ch.verno.ui.base.components.entry.twooption.VATwoOptionEntry;
import ch.verno.ui.base.components.entry.weekoption.VAWeekOption;
import ch.verno.ui.base.components.schedulepicker.VAScheduleWeekPicker;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Setter;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.Validator;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.i18n.I18NProvider;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.function.Function;

public class EntryFactory<DTO> {

  @Nullable
  private final I18NProvider i18nProvider;

  public EntryFactory(@Nullable final I18NProvider i18nProvider) {
    this.i18nProvider = i18nProvider;
  }

  @Nonnull
  private String getTranslation(@Nonnull final String key) {
    if (i18nProvider != null) {
      final Locale locale = UI.getCurrent() != null ? UI.getCurrent().getLocale() : Locale.getDefault();
      return i18nProvider.getTranslation(key, locale);
    }
    return key;
  }

  @Nonnull
  public VATextField createTextEntry(@Nonnull final ValueProvider<DTO, String> valueProvider,
                                     @Nonnull final Setter<DTO, String> valueSetter,
                                     @Nonnull final Binder<DTO> binder,
                                     @Nonnull final Optional<String> required,
                                     @Nonnull final String label) {
    return createTextEntry(valueProvider, valueSetter, binder, required, label, false);
  }

  @Nonnull
  public VATextField createTextEntry(@Nonnull final ValueProvider<DTO, String> valueProvider,
                                     @Nonnull final Setter<DTO, String> valueSetter,
                                     @Nonnull final Binder<DTO> binder,
                                     @Nonnull final Optional<String> required,
                                     @Nonnull final String label,
                                     final boolean showClearButton) {
    final var textField = new VATextField(label);
    textField.setWidthFull();
    textField.setClearButtonVisible(showClearButton);
    textField.setValueChangeMode(ValueChangeMode.EAGER);
    bindEntry(textField, valueProvider, valueSetter, binder, required);
    return textField;
  }

  @Nonnull
  public TextArea createTextAreaEntry(@Nonnull final ValueProvider<DTO, String> valueProvider,
                                      @Nonnull final Setter<DTO, String> valueSetter,
                                      @Nonnull final Binder<DTO> binder,
                                      @Nonnull final Optional<String> required,
                                      @Nonnull final String label) {
    final var textArea = new TextArea(label);
    textArea.setWidthFull();
    textArea.setValueChangeMode(ValueChangeMode.EAGER);
    bindEntry(textArea, valueProvider, valueSetter, binder, required);
    return textArea;
  }

  @Nonnull
  public PasswordField createPasswordField(@Nonnull final ValueProvider<DTO, String> valueProvider,
                                           @Nonnull final Setter<DTO, String> valueSetter,
                                           @Nonnull final Binder<DTO> binder,
                                           @Nonnull final Optional<String> required,
                                           @Nonnull final String label,
                                           final boolean newMode) {
    final var passwordField = new PasswordField(label);
    passwordField.setWidthFull();

    if (!newMode) {
      passwordField.setReadOnly(true);
      passwordField.setTooltipText("Password can only be set when creating a new user. To change the password of an existing user, please use the 'Change Password' function via right-click on the user in the users grid.");
      passwordField.setRevealButtonVisible(false);
    } else {
      passwordField.setReadOnly(false);
      passwordField.setTooltipText(Publ.EMPTY_STRING);
      passwordField.setRevealButtonVisible(true);
    }

    passwordField.setValueChangeMode(ValueChangeMode.EAGER);
    bindEntry(passwordField, valueProvider, valueSetter, binder, required);
    return passwordField;
  }

  @Nonnull
  public EmailField createEmailEntry(@Nonnull final ValueProvider<DTO, String> valueProvider,
                                     @Nonnull final Setter<DTO, String> valueSetter,
                                     @Nonnull final Binder<DTO> binder,
                                     @Nonnull final Optional<String> required,
                                     @Nonnull final String label) {
    final var emailField = new EmailField(label);
    emailField.setClearButtonVisible(true);
    emailField.setWidthFull();
    emailField.setValueChangeMode(ValueChangeMode.EAGER);

    emailField.setPattern(null);

    final boolean allowEmpty = required.isEmpty();
    bindEntry(
            emailField,
            valueProvider,
            valueSetter,
            binder,
            required,
            (value, ctx) -> {
              if (value == null || value.isBlank()) {
                return allowEmpty
                        ? ValidationResult.ok()
                        : ValidationResult.error(required.orElse(getTranslation("common.required")));
              }
              if (!value.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
                return ValidationResult.error(getTranslation("base.please.enter.a.valid.email.address"));
              }
              return ValidationResult.ok();
            }
    );

    return emailField;
  }

  @Nonnull
  public PhoneEntry createPhoneNumberEntry(@Nonnull final ValueProvider<DTO, PhoneNumber> valueProvider,
                                           @Nonnull final Setter<DTO, PhoneNumber> valueSetter,
                                           @Nonnull final Binder<DTO> binder,
                                           @Nonnull final Optional<String> required,
                                           @Nonnull final String label) {
    final var phoneEntry = new PhoneEntry(label);
    phoneEntry.setWidthFull();

    final var binding = binder.forField(phoneEntry);
    required.ifPresent(binding::asRequired);
    binding.withValidator(
            pn -> pn == null
                    || pn.isEmpty()
                    || PhoneNumberFormatter.isValid(
                    pn.phoneNumber(),
                    pn.callingCode().regionCode()
            ),
            getTranslation("base.invalid.phone.number")
    );
    binding.bind(valueProvider, valueSetter);

    return phoneEntry;
  }

  @Nonnull
  public DatePicker createDateEntry(@Nonnull final ValueProvider<DTO, LocalDate> valueProvider,
                                    @Nonnull final Setter<DTO, LocalDate> valueSetter,
                                    @Nonnull final Binder<DTO> binder,
                                    @Nonnull final Optional<String> required,
                                    @Nonnull final String label) {
    final var datePicker = new DatePicker(label);
    datePicker.setLocale(Locale.GERMAN);
    datePicker.setWidthFull();
    bindEntry(datePicker, valueProvider, valueSetter, binder, required);
    return datePicker;
  }

  @Nonnull
  public VANumberField createNumberEntry(@Nonnull final ValueProvider<DTO, Double> valueProvider,
                                         @Nonnull final Setter<DTO, Double> valueSetter,
                                         @Nonnull final Binder<DTO> binder,
                                         @Nonnull final Optional<String> required,
                                         @Nonnull final String label) {
    final var numberField = new VANumberField(label);
    numberField.setValueChangeMode(ValueChangeMode.EAGER);
    numberField.setWidthFull();

    bindEntry(numberField, valueProvider, valueSetter, binder, required, getDoubleValidator(required, required.isEmpty()));

    return numberField;
  }

  @Nonnull
  public VAComboBox<Long> createComboBoxEntry(@Nonnull final ValueProvider<DTO, Long> valueProvider,
                                              @Nonnull final Setter<DTO, Long> valueSetter,
                                              @Nonnull final Binder<DTO> binder,
                                              @Nonnull final Optional<String> required,
                                              @Nonnull final String label,
                                              @Nonnull final Map<Long, String> options) {
    final var comboBox = new VAComboBox<Long>(label);
    comboBox.setWidthFull();

    comboBox.setItems(options.keySet());
    comboBox.setItemLabelGenerator(id -> options.getOrDefault(id, String.valueOf(id)));
    comboBox.setClearButtonVisible(true);

    bindEntry(comboBox, valueProvider, valueSetter, binder, required);

    return comboBox;
  }

  @Nonnull
  public <E extends Enum<E>> VAComboBox<E> createEnumComboBoxEntry(@Nonnull final ValueProvider<DTO, E> valueProvider,
                                                                   @Nonnull final Setter<DTO, E> valueSetter,
                                                                   @Nonnull final Binder<DTO> binder,
                                                                   @Nonnull final E[] values,
                                                                   @Nonnull final Optional<String> required,
                                                                   @Nonnull final String label,
                                                                   @Nonnull final Function<E, String> labelProvider) {
    final var comboBox = new VAComboBox<E>(label);
    comboBox.setWidthFull();

    comboBox.setItems(values);
    comboBox.setItemLabelGenerator(labelProvider::apply);
    comboBox.setClearButtonVisible(true);

    bindEntry(comboBox, valueProvider, valueSetter, binder, required);

    return comboBox;
  }

  @Nonnull
  public TimePicker createTimeEntry(@Nonnull final ValueProvider<DTO, LocalTime> valueProvider,
                                    @Nonnull final Setter<DTO, LocalTime> valueSetter,
                                    @Nonnull final Binder<DTO> binder,
                                    @Nonnull final Optional<String> required,
                                    @Nonnull final String label) {
    final var timePicker = new TimePicker(label);
    timePicker.setWidthFull();
    timePicker.setLocale(Locale.GERMAN);
    timePicker.setClearButtonVisible(true);

    timePicker.setStep(Duration.ofMinutes(5));

    bindEntry(timePicker, valueProvider, valueSetter, binder, required);
    return timePicker;
  }

  @Nonnull
  public Checkbox createCheckBoxEntry(@Nonnull final ValueProvider<DTO, Boolean> valueProvider,
                                      @Nonnull final Setter<DTO, Boolean> valueSetter,
                                      @Nonnull final Binder<DTO> binder,
                                      @Nonnull final String label) {
    final var checkbox = new Checkbox(label);
    checkbox.setWidthFull();
    bindEntry(checkbox, valueProvider, valueSetter, binder, Optional.empty());
    return checkbox;
  }

  @Nonnull
  public <T> MultiSelectComboBox<T> createMultiSelectComboBoxEntry(@Nonnull final ValueProvider<DTO, List<T>> valueProvider,
                                                                   @Nonnull final Setter<DTO, List<T>> valueSetter,
                                                                   @Nonnull final Binder<DTO> binder,
                                                                   @Nonnull final Optional<String> required,
                                                                   @Nonnull final String label,
                                                                   @Nonnull final Collection<T> options,
                                                                   @Nonnull final ValueProvider<T, String> optionLabelProvider) {
    final var comboBox = new MultiSelectComboBox<T>(label);
    comboBox.setWidthFull();

    comboBox.setItems(options);
    comboBox.setItemLabelGenerator(optionLabelProvider::apply);
    comboBox.setClearButtonVisible(true);

    final boolean allowEmpty = required.isEmpty();

    bindEntry(
            comboBox,
            dto -> new LinkedHashSet<>(valueProvider.apply(dto)),
            (dto, selectedSet) -> valueSetter.accept(dto, new ArrayList<>(selectedSet)),
            binder,
            required,
            (value, ctx) -> {
              if (value == null || value.isEmpty()) {
                return allowEmpty
                        ? ValidationResult.ok()
                        : ValidationResult.error(required.orElse(getTranslation("base.select.at.least.one.item")));
              }
              return ValidationResult.ok();
            }
    );

    return comboBox;
  }

  @Nonnull
  public VATwoOptionEntry<GenderDto> createGenderEntry(@Nonnull final ValueProvider<DTO, GenderDto> valueProvider,
                                                       @Nonnull final Setter<DTO, GenderDto> valueSetter,
                                                             @Nonnull final Binder<DTO> binder,
                                                       @Nonnull final List<GenderDto> options,
                                                       @Nonnull final ValueProvider<GenderDto, String> optionLabelProvider,
                                                             @Nonnull final Optional<String> required,
                                                             @Nonnull final String label) {
    final var entry = new VATwoOptionEntry<>(label, options, optionLabelProvider);
    bindEntry(entry, valueProvider, valueSetter, binder, required);
    entry.setWidthFull();
    return entry;
  }

  @Nonnull
  public VAColorPicker createColorPickerEntry(@Nonnull final ValueProvider<DTO, String> valueProvider,
                                              @Nonnull final Setter<DTO, String> valueSetter,
                                              @Nonnull final Binder<DTO> binder,
                                              @Nonnull final Optional<String> required,
                                              @Nonnull final String label) {
    final var colorPicker = new VAColorPicker(label);
    colorPicker.setPresets(ColorPresets.getDefaultColorPresets());
    colorPicker.setWidthFull();
    bindEntry(colorPicker, valueProvider, valueSetter, binder, required);
    return colorPicker;
  }

  @Nonnull
  public VAWeekOption createWeekOptionEntry(@Nonnull final ValueProvider<DTO, List<DayOfWeek>> valueProvider,
                                            @Nonnull final Setter<DTO, List<DayOfWeek>> valueSetter,
                                            @Nonnull final Binder<DTO> binder,
                                            @Nonnull final Optional<String> required,
                                            @Nonnull final String label) {
    final var weekOption = new VAWeekOption(label);
    weekOption.setWidthFull();

    final boolean allowEmpty = required.isEmpty();

    bindEntry(
            weekOption,
            valueProvider,
            valueSetter,
            binder,
            required,
            (value, valueContext) -> {
              if (value == null || value.isEmpty()) {
                return allowEmpty ? ValidationResult.ok()
                        : ValidationResult.error(required.orElse(getTranslation("base.select.at.least.one.day")));
              }
              return ValidationResult.ok();
            }
    );

    return weekOption;
  }

  @Nonnull
  public VAScheduleWeekPicker createScheduleWeekPickerEntry(@Nonnull final ValueProvider<DTO, Set<YearWeekDto>> valueProvider,
                                                            @Nonnull final Setter<DTO, Set<YearWeekDto>> valueSetter,
                                                            @Nonnull final Binder<DTO> binder,
                                                            @Nonnull final Optional<String> required,
                                                            @Nonnull final String label) {
    final var scheduleWeekPicker = new VAScheduleWeekPicker(label);
    scheduleWeekPicker.setWidthFull();

    bindEntry(scheduleWeekPicker,
            valueProvider,
            valueSetter,
            binder,
            required,
            (value, ctx) -> {
              if (value == null || value.isEmpty()) {
                return required.isEmpty()
                        ? ValidationResult.ok()
                        : ValidationResult.error(required.orElse(getTranslation("base.select.at.least.one.week")));
              }
              return ValidationResult.ok();
            }
    );

    return scheduleWeekPicker;
  }

  @Nonnull
  private Validator<Double> getDoubleValidator(@Nonnull final Optional<String> required,
                                               final boolean allowNull) {
    return (value, valueContext) -> {
      if (value == null) {
        return allowNull ? ValidationResult.ok()
                : ValidationResult.error(required.orElse(getTranslation("common.required")));
      }
      return value > 0
              ? ValidationResult.ok()
              : ValidationResult.error(getTranslation("base.value.must.be.greater.than.0"));
    };
  }

  @Nonnull
  private Validator<Long> getLongValidator(@Nonnull final Optional<String> required,
                                           final boolean allowNull) {
    return (value, valueContext) -> {
      if (value == null) {
        return allowNull ? ValidationResult.ok()
                : ValidationResult.error(required.orElse(getTranslation("common.required")));
      }
      return value > 0
              ? ValidationResult.ok()
              : ValidationResult.error(getTranslation("base.value.must.be.greater.than.0"));
    };
  }

  private <FIELDVALUE> void bindEntry(@Nonnull final HasValue<?, FIELDVALUE> entry,
                                      @Nonnull final ValueProvider<DTO, FIELDVALUE> valueProvider,
                                      @Nonnull final Setter<DTO, FIELDVALUE> valueSetter,
                                      @Nonnull final Binder<DTO> binder,
                                      @Nonnull final Optional<String> required) {
    final var binding = binder.forField(entry);
    required.ifPresent(binding::asRequired);

    // has to be the last call in the chain
    binding.bind(valueProvider, valueSetter);
  }

  private <FIELDVALUE> void bindEntry(@Nonnull final HasValue<?, FIELDVALUE> entry,
                                      @Nonnull final ValueProvider<DTO, FIELDVALUE> valueProvider,
                                      @Nonnull final Setter<DTO, FIELDVALUE> valueSetter,
                                      @Nonnull final Binder<DTO> binder,
                                      @Nonnull final Optional<String> required,
                                      @Nonnull final Validator<FIELDVALUE> validator) {
    var binding = binder.forField(entry);
    required.ifPresent(binding::asRequired);
    binding = binding.withValidator(validator);

    // has to be the last call in the chain
    binding.bind(valueProvider, valueSetter);
  }

}
