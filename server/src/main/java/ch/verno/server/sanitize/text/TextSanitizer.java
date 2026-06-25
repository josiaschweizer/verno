package ch.verno.server.sanitize.text;

import ch.verno.contract.dto.table.text.TextDto;
import ch.verno.lib.lib.language.Language;
import ch.verno.server.util.ServerStringUtil;
import jakarta.annotation.Nonnull;

public class TextSanitizer {

  @Nonnull
  public static TextDto sanitize(@Nonnull final TextDto dto) {
    final var sanitized = TextDto.empty();

    sanitized.setId(dto.getId());
    sanitized.setIdentifier(ServerStringUtil.safeString(dto.getIdentifier()));
    sanitized.setSubIdentifier(ServerStringUtil.safeString(dto.getSubIdentifier()));
    sanitized.setLanguage(Language.fromCode(ServerStringUtil.safeString(dto.getLanguage().getCode())));
    sanitized.setText(ServerStringUtil.safeString(dto.getText()));

    return sanitized;
  }

}
