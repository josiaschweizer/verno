package ch.verno.ui.base.factory;

import ch.verno.common.base.components.entry.phonenumber.PhoneNumber;
import ch.verno.common.util.phonenumber.PhoneNumberFormatter;
import ch.verno.ui.base.components.entry.phonenumber.PhoneEntry;
import ch.verno.ui.base.components.entry.twooption.TwoOptionEntry;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Setter;
import com.vaadin.flow.function.ValueProvider;
import jakarta.annotation.Nonnull;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public class EntryFactory<DTO, TWOSELECTIONDTO> {


  @Nonnull
  public TextField createTextEntry(@Nonnull final ValueProvider<DTO, String> valueProvider,
                                   @Nonnull final Setter<DTO, String> valueSetter,
                                   @Nonnull final Binder<DTO> binder,
                                   @Nonnull final Optional<String> required,
                                   @Nonnull final String label) {
    final var textField = new TextField(label);
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
  public ComboBox<Long> createComboBoxEntry(@Nonnull final ValueProvider<DTO, Long> valueProvider,
                                            @Nonnull final Setter<DTO, Long> valueSetter,
                                            @Nonnull final Binder<DTO> binder,
                                            @Nonnull final Optional<String> required,
                                            @Nonnull final String label,
                                            @Nonnull final Map<Long, String> options) {
    final var comboBox = new ComboBox<Long>(label);
    comboBox.setWidthFull();

    comboBox.setItems(options.keySet());
    comboBox.setItemLabelGenerator(id -> options.getOrDefault(id, String.valueOf(id)));
    comboBox.setClearButtonVisible(true);

    bindEntry(comboBox, valueProvider, valueSetter, binder, required);

    return comboBox;
  }

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

}
