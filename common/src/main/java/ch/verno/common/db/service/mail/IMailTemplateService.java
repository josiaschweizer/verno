package ch.verno.common.db.service.mail;

import ch.verno.common.db.dto.table.mail.MailTemplateDto;
import jakarta.annotation.Nonnull;

import java.util.List;

public interface IMailTemplateService {
  @Nonnull
  MailTemplateDto upsertTemplate(@Nonnull MailTemplateDto dto);

  @Nonnull
  MailTemplateDto getTemplateByKey(@Nonnull String templateKey);

  boolean hasTemplateByKey(@Nonnull String templateKey);

  @Nonnull
  List<MailTemplateDto> getAllTemplatesForCurrentTenant();

  void deleteTemplate(@Nonnull String templateKey);
}
