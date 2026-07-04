package ch.verno.contract.mail.placeholder.base;

import jakarta.annotation.Nonnull;

import java.util.Map;
import java.util.function.Function;

public abstract class BasePlaceholderMapping<T extends MailContext, PLACEHOLDER extends Placeholder> {

  @Nonnull
  public Function<T, String> getMappingByPlaceholder(@Nonnull final PLACEHOLDER placeholder) {
    return getMapping().get(placeholder);
  }

  @Nonnull
  public abstract Map<PLACEHOLDER, Function<T, String>> getMapping();

}
