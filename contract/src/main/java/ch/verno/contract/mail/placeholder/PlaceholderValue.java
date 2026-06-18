package ch.verno.contract.mail.placeholder;

import ch.verno.contract.mail.placeholder.context.MailContext;
import jakarta.annotation.Nonnull;

import java.util.function.Function;

public record PlaceholderValue<C extends MailContext>(
        @Nonnull Placeholder placeholder,
        @Nonnull Function<C, String> valueFunction) {
}
