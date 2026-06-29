package ch.verno.ui.base.components.filter;

import ch.verno.lib.Publ;
import ch.verno.ui.base.components.entry.textfield.VATextField;
import ch.verno.ui.base.os.OSUtil;
import ch.verno.ui.base.shortcut.DefaultVernoShortcuts;
import ch.verno.ui.base.shortcut.RegisterShortcutUtil;
import ch.verno.ui.base.shortcut.ShortcutDisplayComponent;
import ch.verno.ui.base.shortcut.registry.ShortcutController;
import com.google.inject.Injector;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.data.value.ValueChangeMode;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public class VASearchFilter extends CustomField<String> {

  @Nonnull private final VATextField textField;

  @Nullable private String currentValue;

  public VASearchFilter(@Nonnull final Injector injector) {
    this(injector, null, null);
  }

  public VASearchFilter(@Nonnull final Injector injector,
                        @Nullable final String placeholder) {
    this(injector, null, placeholder);
  }

  public VASearchFilter(@Nonnull final Injector injector,
                        @Nullable final String label,
                        @Nullable final String placeholder) {
    setWidthFull();

    textField = new VATextField();
    textField.setValueChangeMode(ValueChangeMode.EAGER);
    textField.setWidthFull();
    textField.setClearButtonVisible(true);

    if (label != null) {
      textField.setLabel(label);
    }

    if (placeholder != null) {
      textField.setPlaceholder(placeholder);
    } else {
      textField.setPlaceholder(getTranslation("base.search"));
    }

    textField.addValueChangeListener(event -> {
      final var newValue = event.getValue();
      if (newValue == null || newValue.isEmpty()) {
        currentValue = null;
      } else {
        currentValue = newValue;
      }
      setValue(currentValue);
    });
    final var focusShortcut = DefaultVernoShortcuts.FOCUS;
    if (!OSUtil.getOs().isMobile()) {
      final var registration = RegisterShortcutUtil.addFocusShortcut(textField, focusShortcut);
      textField.setSuffixComponent(ShortcutDisplayComponent.of(focusShortcut));

      injector.getInstance(ShortcutController.class).register(focusShortcut, textField::focus, textField, registration);
    }

    add(textField);
  }

  @Override
  public void setLabel(final String label) {
    textField.setLabel(label);
  }

  public void setFilterWidth(@Nonnull final String width) {
    textField.setWidth(width);
  }

  public void setMaxLength(final int maxLength) {
    textField.setMaxLength(maxLength);
  }

  @Nonnull
  public VATextField getTextField() {
    return textField;
  }

  @Nullable
  public String getFilterValue() {
    return currentValue;
  }

  public boolean isEmpty() {
    return currentValue == null || currentValue.isEmpty();
  }

  @Nullable
  @Override
  protected String generateModelValue() {
    return currentValue;
  }

  @Override
  protected void setPresentationValue(@Nullable final String value) {
    currentValue = value;
    if (value == null || value.isEmpty()) {
      textField.setValue(Publ.EMPTY_STRING);
    } else {
      textField.setValue(value);
    }
  }

  @Override
  public void setEnabled(final boolean enabled) {
    super.setEnabled(enabled);
    textField.setEnabled(enabled);
  }
}

