package ch.verno.common.db.service.mail;

import ch.verno.common.db.dto.table.mail.MailLogDto;
import ch.verno.common.db.enums.mail.MailLogStatus;
import jakarta.annotation.Nonnull;
import org.simplejavamail.api.email.Email;

import java.util.List;

public interface IMailLogService {

  @Nonnull
  MailLogDto create(@Nonnull MailLogDto dto);

  @Nonnull
  MailLogDto getById(@Nonnull Long id);

  @Nonnull
  List<MailLogDto> getAll();

  @Nonnull
  List<MailLogDto> getAllByStatus(@Nonnull MailLogStatus status);

  @Nonnull
  List<MailLogDto> getAllByRecipientEmail(@Nonnull String recipientEmail);

  @Nonnull
  MailLogDto update(@Nonnull Long id, @Nonnull MailLogDto dto);

  @Nonnull
  MailLogDto logSent(@Nonnull Email mail, @Nonnull String providerMessageId);

  @Nonnull
  MailLogDto logSent(@Nonnull String recipientEmail,
                     @Nonnull String recipientName,
                     @Nonnull String templateName,
                     @Nonnull String subject,
                     @Nonnull String content,
                     @Nonnull String placeholdersJson,
                     @Nonnull String providerMessageId,
                     Long createdBy);

  MailLogDto logFailed(@Nonnull Email mail, @Nonnull String errorMessage);

  @Nonnull
  MailLogDto logFailed(@Nonnull String recipientEmail,
                       @Nonnull String recipientName,
                       @Nonnull String templateName,
                       @Nonnull String subject,
                       @Nonnull String content,
                       @Nonnull String placeholdersJson,
                       @Nonnull String errorMessage,
                       Long createdBy);
}
