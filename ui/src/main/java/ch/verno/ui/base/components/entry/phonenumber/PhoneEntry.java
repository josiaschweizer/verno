package ch.verno.ui.base.components.entry.phonenumber;

import ch.verno.common.ui.base.components.entry.phonenumber.PhoneNumber;
import ch.verno.publ.Publ;
import ch.verno.common.lib.calling.CallingCode;
import ch.verno.common.lib.calling.CallingCodeHelper;
import ch.verno.common.lib.phonenumber.PhoneNumberFormatter;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import jakarta.annotation.Nonnull;

public class PhoneEntry extends CustomField<PhoneNumber> {

  private final ComboBox<CallingCode> callingCodes;
  private final TextField phoneNumberField;

  public PhoneEntry(@Nonnull final String label) {
    setLabel(label);

    final var callingCodeOptions = CallingCodeHelper.getCallingCodes();
    var chValue = callingCodeOptions.size() > 1 ? callingCodeOptions.getFirst() : CallingCode.empty();
    for (final var code : callingCodeOptions) {
      if (code.countryCode() == 41) {
        chValue = code;
        break;
      }
    }

    callingCodes = new ComboBox<>();
    callingCodes.setItems(callingCodeOptions);
    callingCodes.setItemLabelGenerator(CallingCode::display);
    callingCodes.setWidth("100px");
    callingCodes.setValue(chValue);

    phoneNumberField = new TextField();
    phoneNumberField.setAllowedCharPattern("[0-9 ]");
    phoneNumberField.setPlaceholder("77 432 06 26");
    phoneNumberField.setWidthFull();

    phoneNumberField.addValueChangeListener(event -> {
      final var callingCode = callingCodes.getValue();
      if (callingCode == null) {
        return;
      }

      final var code = String.valueOf(callingCode.countryCode());
      var digits = event.getValue().replaceAll("\\D", Publ.EMPTY_STRING);

      if (digits.isEmpty()) {
        phoneNumberField.clear();
        return;
      }

      final var zeroZeroPrefix = "00" + code;

      if (digits.startsWith(zeroZeroPrefix)) {
        digits = digits.substring(zeroZeroPrefix.length());
      } else if (digits.startsWith(code) && digits.length() > code.length()) {
        digits = digits.substring(code.length());
      } else if (digits.startsWith("0")) {
        digits = digits.substring(1);
      }

      if (digits.isEmpty()) {
        phoneNumberField.clear();
        return;
      }

      final var formatted = PhoneNumberFormatter.formatInternationalWithoutCallingCode(
          digits,
          callingCode.regionCode(),
          callingCode.countryCode()
      );

      if (!formatted.equals(phoneNumberField.getValue())) {
        phoneNumberField.setValue(formatted);
      }
    });

    HorizontalLayout layout = new HorizontalLayout(callingCodes, phoneNumberField);
    layout.setWidthFull();
    layout.setPadding(false);
    layout.setSpacing(true);
    layout.setFlexGrow(1, phoneNumberField);

    add(layout);
  }

  @Override
  protected PhoneNumber generateModelValue() {
    if (callingCodes.getValue() == null || phoneNumberField.isEmpty()) {
      return null;
    }

    return new PhoneNumber(
        callingCodes.getValue(),
        phoneNumberField.getValue()
    );
  }

  @Override
  protected void setPresentationValue(PhoneNumber value) {
    if (value == null || value.isEmpty()) {
      // Keep the default calling code from the constructor and just clear the number.
      phoneNumberField.clear();
      return;
    }

    callingCodes.setValue(value.callingCode());
    phoneNumberField.setValue(value.phoneNumber());
  }

  @Override
  public void setValue(final PhoneNumber value) {
    super.setValue(value);
  }

  @Override
  public void setEnabled(final boolean enabled) {
    super.setEnabled(enabled);
    callingCodes.setEnabled(enabled);
    phoneNumberField.setEnabled(enabled);
  }

  @Override
  public void setReadOnly(final boolean readOnly) {
    super.setReadOnly(readOnly);
    callingCodes.setReadOnly(readOnly);
    phoneNumberField.setReadOnly(readOnly);
  }
}