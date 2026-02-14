package ch.verno.ui.verno;

import ch.verno.common.ui.base.components.entry.phonenumber.PhoneNumber;
import ch.verno.common.db.dto.table.GenderDto;
import ch.verno.ui.base.components.entry.phonenumber.PhoneEntry;
import ch.verno.ui.base.components.entry.twooption.VATwoOptionEntry;
import ch.verno.ui.base.factory.EntryFactory;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Setter;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.i18n.I18NProvider;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class FieldFactory<T> {

  @Nonnull
  private final EntryFactory<T> entryFactory;
  @Nullable
  private final I18NProvider i18nProvider;

  public FieldFactory(@Nonnull final EntryFactory<T> entryFactory) {
    this(entryFactory, null);
  }

  public FieldFactory(@Nonnull final EntryFactory<T> entryFactory,
                      @Nullable final I18NProvider i18nProvider) {
    this.entryFactory = entryFactory;
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
  public TextField createFirstNameField(@Nonnull final ValueProvider<T, String> valueProvider,
                                        @Nonnull final Setter<T, String> valueSetter,
                                        @Nonnull final Binder<T> binder) {
    return entryFactory.createTextEntry(
            valueProvider,
            valueSetter,
            binder,
            Optional.of(getTranslation("shared.first.name.is.required")),
            getTranslation("shared.first.name"));
  }

  @Nonnull
  public TextField createLastNameField(@Nonnull final ValueProvider<T, String> valueProvider,
                                       @Nonnull final Setter<T, String> valueSetter,
                                       @Nonnull final Binder<T> binder) {
    return entryFactory.createTextEntry(
            valueProvider,
            valueSetter,
            binder,
            Optional.of(getTranslation("shared.last.name.is.required")),
            getTranslation("shared.last.name")
    );
  }

  @Nonnull
  public DatePicker createBirthDateField(@Nonnull final ValueProvider<T, LocalDate> valueProvider,
                                         @Nonnull final Setter<T, LocalDate> valueSetter,
                                         @Nonnull final Binder<T> binder) {
    final var entry = entryFactory.createDateEntry(
            valueProvider,
            valueSetter,
            binder,
            Optional.empty(),
            getTranslation("shared.birthdate")
    );
    entry.setWidthFull();
    return entry;
  }

  @Nonnull
  public VATwoOptionEntry<GenderDto> createGenderField(@Nonnull final ValueProvider<T, GenderDto> valueProvider,
                                                       @Nonnull final Setter<T, GenderDto> valueSetter,
                                                       @Nonnull final Binder<T> binder,
                                                       @Nonnull final List<GenderDto> genderOptions) {
    return entryFactory.createGenderEntry(
            valueProvider,
            valueSetter,
            binder,
            genderOptions,
            GenderDto::getName,
            Optional.empty(),
            getTranslation("shared.gender")
    );
  }

  @Nonnull
  public EmailField createEmailField(@Nonnull final ValueProvider<T, String> valueProvider,
                                     @Nonnull final Setter<T, String> valueSetter,
                                     @Nonnull final Binder<T> binder) {
    return entryFactory.createEmailEntry(
            valueProvider,
            valueSetter,
            binder,
            Optional.of(getTranslation("shared.email.is.required")),
            getTranslation("shared.e.mail")
    );
  }

  @Nonnull
  public PhoneEntry createPhoneNumberField(@Nonnull final ValueProvider<T, PhoneNumber> valueProvider,
                                           @Nonnull final Setter<T, PhoneNumber> valueSetter,
                                           @Nonnull final Binder<T> binder) {
    return entryFactory.createPhoneNumberEntry(
            valueProvider,
            valueSetter,
            binder,
            Optional.empty(),
            getTranslation("shared.phone")
    );
  }

  @Nonnull
  public TextField createStreetField(@Nonnull final ValueProvider<T, String> valueProvider,
                                     @Nonnull final Setter<T, String> valueSetter,
                                     @Nonnull final Binder<T> binder) {
    return entryFactory.createTextEntry(
            valueProvider,
            valueSetter,
            binder,
            Optional.empty(),
            getTranslation("shared.street")
    );
  }

  @Nonnull
  public TextField createHouseNumberField(@Nonnull final ValueProvider<T, String> valueProvider,
                                          @Nonnull final Setter<T, String> valueSetter,
                                          @Nonnull final Binder<T> binder) {
    return entryFactory.createTextEntry(
            valueProvider,
            valueSetter,
            binder,
            Optional.empty(),
            getTranslation("shared.house.number")
    );
  }

  @Nonnull
  public TextField createZipCodeField(@Nonnull final ValueProvider<T, String> valueProvider,
                                      @Nonnull final Setter<T, String> valueSetter,
                                      @Nonnull final Binder<T> binder) {
    return entryFactory.createTextEntry(
            valueProvider,
            valueSetter,
            binder,
            Optional.empty(),
            getTranslation("shared.zip.code")
    );
  }

  @Nonnull
  public TextField createCityField(@Nonnull final ValueProvider<T, String> valueProvider,
                                   @Nonnull final Setter<T, String> valueSetter,
                                   @Nonnull final Binder<T> binder) {
    return entryFactory.createTextEntry(
            valueProvider,
            valueSetter,
            binder,
            Optional.empty(),
            getTranslation("shared.city")
    );
  }

  @Nonnull
  public TextField createCountryField(@Nonnull final ValueProvider<T, String> valueProvider,
                                      @Nonnull final Setter<T, String> valueSetter,
                                      @Nonnull final Binder<T> binder) {
    return entryFactory.createTextEntry(
            valueProvider,
            valueSetter,
            binder,
            Optional.empty(),
            getTranslation("shared.country")
    );
  }
}
