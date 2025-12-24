package ch.verno.ui.base.components.entry.phonenumber;

import ch.verno.common.base.components.entry.phonenumber.PhoneNumber;
import ch.verno.common.util.Publ;
import ch.verno.common.util.calling.CallingCode;
import ch.verno.common.util.calling.CallingCodeHelper;
import ch.verno.common.util.phonenumber.PhoneNumberFormatter;
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
    phoneNumberField.setPlaceholder("Phone number");
    phoneNumberField.setWidthFull();

    phoneNumberField.addValueChangeListener(event -> {
      if (!event.isFromClient()) {
        return;
      }

      final var callingCode = callingCodes.getValue();
      if (callingCode == null) {
        return;
      }

      final var raw = event.getValue().replaceAll("\\D", Publ.EMPTY_STRING);
      final var region = callingCode.regionCode();

      phoneNumberField.setValue(PhoneNumberFormatter.formatNational(raw, region));
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
}