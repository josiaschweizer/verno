package ch.verno.server.bo.mail;

import ch.verno.common.type.mail.MailLogStatus;
import ch.verno.contract.dto.table.mail.MailLogDto;
import ch.verno.lib.Publ;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.service.intern.table.mail.MailLogService;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.simplejavamail.api.email.Email;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;

@Component
public class MailLogBo {

  @Nonnull private final MailLogService mailLogService;

  public MailLogBo(@Nonnull final ServerBean serverBean) {
    this.mailLogService = serverBean.get(MailLogService.class);
  }

  @Nonnull
  public MailLogDto logSent(@Nonnull final Email mail,
                            @Nonnull final String providerMessageId) {
    return log(
            mail,
            MailLogStatus.SENT,
            Publ.EMPTY_STRING,
            providerMessageId
    );
  }

  @Nonnull
  public MailLogDto logFailed(@Nonnull final Email mail,
                              @Nonnull final String errorMessage) {
    return log(
            mail,
            MailLogStatus.FAILED,
            errorMessage,
            Publ.EMPTY_STRING
    );
  }

  @Nonnull
  public MailLogDto logSent(@Nonnull final String recipientEmail,
                            @Nonnull final String recipientName,
                            @Nonnull final String templateName,
                            @Nonnull final String subject,
                            @Nonnull final String content,
                            @Nullable final String placeholdersJson,
                            @Nonnull final String providerMessageId,
                            @Nullable final Long createdBy) {
    return createLog(
            recipientEmail,
            recipientName,
            templateName,
            subject,
            content,
            placeholdersJson,
            MailLogStatus.SENT,
            Publ.EMPTY_STRING,
            providerMessageId,
            createdBy
    );
  }

  @Nonnull
  public MailLogDto logFailed(@Nonnull final String recipientEmail,
                              @Nonnull final String recipientName,
                              @Nonnull final String templateName,
                              @Nonnull final String subject,
                              @Nonnull final String content,
                              @Nullable final String placeholdersJson,
                              @Nonnull final String errorMessage,
                              @Nullable final Long createdBy) {
    return createLog(
            recipientEmail,
            recipientName,
            templateName,
            subject,
            content,
            placeholdersJson,
            MailLogStatus.FAILED,
            errorMessage,
            Publ.EMPTY_STRING,
            createdBy
    );
  }

  @Nonnull
  private MailLogDto log(@Nonnull final Email mail,
                         @Nonnull final MailLogStatus status,
                         @Nonnull final String errorMessage,
                         @Nonnull final String providerMessageId) {
    final var recipient = mail.getRecipients().isEmpty()
            ? null
            : mail.getRecipients().getFirst();

    return createLog(
            recipient != null ? recipient.getAddress() : Publ.EMPTY_STRING,
            recipient != null
                    ? Objects.requireNonNullElse(recipient.getName(), Publ.EMPTY_STRING)
                    : Publ.EMPTY_STRING,
            Publ.EMPTY_STRING,
            Objects.requireNonNullElse(mail.getSubject(), Publ.EMPTY_STRING),
            getContent(mail),
            null,
            status,
            errorMessage,
            providerMessageId,
            null
    );
  }

  @Nonnull
  private String getContent(@Nonnull final Email mail) {
    if (mail.getHTMLText() != null) {
      return mail.getHTMLText();
    }

    return Objects.requireNonNullElse(mail.getPlainText(), Publ.EMPTY_STRING);
  }

  @Nonnull
  private MailLogDto createLog(@Nonnull final String recipientEmail,
                               @Nonnull final String recipientName,
                               @Nonnull final String templateName,
                               @Nonnull final String subject,
                               @Nonnull final String content,
                               @Nullable final String placeholdersJson,
                               @Nonnull final MailLogStatus status,
                               @Nonnull final String errorMessage,
                               @Nonnull final String providerMessageId,
                               @Nullable final Long createdBy) {
    final var now = LocalDateTime.now();
    final var dto = new MailLogDto(
            recipientEmail,
            recipientName,
            templateName,
            subject,
            content,
            placeholdersJson,
            status,
            errorMessage,
            providerMessageId,
            status == MailLogStatus.SENT ? now : null,
            now,
            createdBy
    );
    return mailLogService.create(dto);
  }
}