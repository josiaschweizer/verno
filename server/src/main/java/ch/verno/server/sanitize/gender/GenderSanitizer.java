package ch.verno.server.sanitize.gender;

import ch.verno.contract.dto.table.gender.GenderDto;
import ch.verno.server.sanitize.text.TextSanitizer;
import ch.verno.server.util.ServerStringUtil;
import jakarta.annotation.Nonnull;

import java.util.Map;
import java.util.stream.Collectors;

public class GenderSanitizer {

  @Nonnull
  public static GenderDto sanitize(@Nonnull final GenderDto dto) {
    final var sanitized = GenderDto.empty();

    sanitized.setId(dto.getId());
    sanitized.setName(ServerStringUtil.safeString(dto.getName()));
    sanitized.setDescription(ServerStringUtil.safeString(dto.getDescription()));
    sanitized.setUserDisplayTexts(
            dto.getUserDisplayTexts() == null
                    ? null
                    : dto.getUserDisplayTexts().entrySet().stream()
                    .collect(Collectors.toMap(
                            Map.Entry::getKey,
                            entry -> TextSanitizer.sanitize(entry.getValue())
                    ))
    );

    return sanitized;
  }

}
