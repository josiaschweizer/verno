package ch.verno.contract.mail.placeholder;

import ch.verno.contract.mail.placeholder.base.MailContext;
import ch.verno.contract.mail.placeholder.base.BasePlaceholderMapping;
import ch.verno.contract.mail.placeholder.base.Placeholder;
import jakarta.annotation.Nonnull;

import java.util.function.Function;

public class PlaceholderResolver<T extends MailContext, PLACEHOLDER extends Placeholder> {

  @Nonnull private final BasePlaceholderMapping<T, PLACEHOLDER> mapping;

  public PlaceholderResolver(@Nonnull final BasePlaceholderMapping<T, PLACEHOLDER> mapping) {
    this.mapping = mapping;
  }

  @Nonnull
  public Function<T, String> getValueFunctionForPlaceholder(@Nonnull final PLACEHOLDER placeholder) {
    return mapping.getMappingByPlaceholder(placeholder);
  }

}
