package ch.verno.server.mapper.gender;

import ch.verno.contract.dto.table.text.TextDto;
import ch.verno.lib.lib.language.Language;
import jakarta.annotation.Nonnull;

import java.util.Map;

public record GenderTranslationContext(@Nonnull Map<Language, TextDto> translations) {
}