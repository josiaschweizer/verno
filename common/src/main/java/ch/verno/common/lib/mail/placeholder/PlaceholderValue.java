package ch.verno.common.lib.mail.placeholder;

import ch.verno.common.lib.mail.placeholder.context.MailContext;
import jakarta.annotation.Nonnull;

import java.util.function.Function;

public record PlaceholderValue<C extends MailContext>(
        @Nonnull Placeholder placeholder,
        @Nonnull Function<C, String> valueFunction) {
}
