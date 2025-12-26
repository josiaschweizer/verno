package ch.verno.ui.base.factory;

import ch.verno.common.base.components.entry.phonenumber.PhoneNumber;
import ch.verno.common.util.phonenumber.PhoneNumberFormatter;
import ch.verno.ui.base.components.entry.combobox.VAComboBox;
import ch.verno.ui.base.components.entry.numberfield.VANumberField;
import ch.verno.ui.base.components.entry.phonenumber.PhoneEntry;
import ch.verno.ui.base.components.entry.textfield.VATextField;
import ch.verno.ui.base.components.entry.twooption.TwoOptionEntry;
import ch.verno.ui.base.components.entry.weekoption.VAWeekOption;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Setter;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.Validator;
import com.vaadin.flow.function.ValueProvider;
import jakarta.annotation.Nonnull;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

public class EntryFactory<DTO, TWOSELECTIONDTO> {


  @Nonnull
  public VATextField createTextEntry(@Nonnull final ValueProvider<DTO, String> valueProvider,
                                   @Nonnull final Setter<DTO, String> valueSetter,
                                   @Nonnull final Binder<DTO> binder,
                                   @Nonnull final Optional<String> required,
                                   @Nonnull final String label) {
    final var textField = new VATextField(label);
    textField.setWidthFull();
    bindEntry(textField, valueProvider, valueSetter, binder, required);
    return textField;
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
    emailField.setErrorMessage("Please enter a valid email address.");
    bindEntry(emailField, valueProvider, valueSetter, binder, required);
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
        "Invalid phone number"
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
    numberField.setWidthFull();

    final boolean allowNull = required.isEmpty();

    bindEntry(
            numberField,
            valueProvider,
            valueSetter,
            binder,
            required,
            (value, valueContext) -> {
              if (value == null) {
                return allowNull ? ValidationResult.ok()
                        : ValidationResult.error(required.orElse("Required"));
              }
              return value > 0
                      ? ValidationResult.ok()
                      : ValidationResult.error("Value must be greater than 0");
            }
    );

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
  public TwoOptionEntry<TWOSELECTIONDTO> createGenderEntry(@Nonnull final ValueProvider<DTO, TWOSELECTIONDTO> valueProvider,
                                                           @Nonnull final Setter<DTO, TWOSELECTIONDTO> valueSetter,
                                                           @Nonnull final Binder<DTO> binder,
                                                           @Nonnull final List<TWOSELECTIONDTO> options,
                                                           @Nonnull final ValueProvider<TWOSELECTIONDTO, String> optionLabelProvider,
                                                           @Nonnull final Optional<String> required,
                                                           @Nonnull final String label) {
    final var entry = new TwoOptionEntry<>(label, options, optionLabelProvider);
    bindEntry(entry, valueProvider, valueSetter, binder, required);
    entry.setWidthFull();
    return entry;
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
                        : ValidationResult.error(required.orElse("Select at least one day"));
              }
              return ValidationResult.ok();
            }
    );

    return weekOption;
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
