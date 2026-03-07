package ch.verno.server.service.intern.mail;

import ch.verno.common.db.dto.table.mail.MailLogDto;
import ch.verno.common.db.enums.mail.MailLogStatus;
import ch.verno.common.db.filter.MailLogFilter;
import ch.verno.common.db.service.mail.IMailLogService;
import ch.verno.common.exceptions.db.DBNotFoundException;
import ch.verno.common.exceptions.db.DBNotFoundReason;
import ch.verno.publ.Publ;
import ch.verno.server.mapper.mail.MailLogMapper;
import ch.verno.server.repository.mail.MailLogRepository;
import ch.verno.server.spec.MailLogSpec;
import ch.verno.server.spec.PageHelper;
import com.vaadin.flow.data.provider.QuerySortOrder;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.simplejavamail.api.email.Email;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class MailLogService implements IMailLogService {

  @Nonnull private final MailLogRepository mailLogRepository;

  @Nonnull private final MailLogSpec mailLogSpec;

  public MailLogService(@Nonnull final MailLogRepository mailLogRepository) {
    this.mailLogRepository = mailLogRepository;

    this.mailLogSpec = new MailLogSpec();
  }

  @Nonnull
  @Override
  @Transactional
  public MailLogDto create(@Nonnull final MailLogDto dto) {
    if (dto.getCreatedAt() == null) {
      dto.setCreatedAt(LocalDateTime.now());
    }

    final var entity = MailLogMapper.toEntity(dto);
    final var savedEntity = mailLogRepository.save(entity);
    return MailLogMapper.toDto(savedEntity);
  }

  @Nonnull
  @Override
  @Transactional(readOnly = true)
  public MailLogDto getById(@Nonnull final Long id) {
    final var entity = mailLogRepository.findById(id)
            .orElseThrow(() -> new DBNotFoundException(DBNotFoundReason.MAIL_LOG_BY_ID_NOT_FOUND));
    return MailLogMapper.toDto(entity);
  }

  @Nonnull
  @Override
  @Transactional(readOnly = true)
  public List<MailLogDto> getAll() {
    return mailLogRepository.findAll()
            .stream()
            .map(MailLogMapper::toDto)
            .toList();
  }

  @Nonnull
  @Override
  @Transactional(readOnly = true)
  public List<MailLogDto> getAllByStatus(@Nonnull final MailLogStatus status) {
    return mailLogRepository.findAllByStatusOrderByCreatedAtDesc(status)
            .stream()
            .map(MailLogMapper::toDto)
            .toList();
  }

  @Nonnull
  @Override
  @Transactional(readOnly = true)
  public List<MailLogDto> getAllByRecipientEmail(@Nonnull final String recipientEmail) {
    return mailLogRepository.findAllByRecipientEmailOrderByCreatedAtDesc(recipientEmail)
            .stream()
            .map(MailLogMapper::toDto)
            .toList();
  }

  @Nonnull
  @Override
  public MailLogDto update(@Nonnull final Long id, @Nonnull final MailLogDto dto) {
    final var entity = mailLogRepository.findById(id)
            .orElseThrow(() -> new DBNotFoundException(DBNotFoundReason.MAIL_LOG_BY_ID_NOT_FOUND));

    MailLogMapper.updateEntity(dto, entity);

    final var savedEntity = mailLogRepository.save(entity);
    return MailLogMapper.toDto(savedEntity);
  }

  public void delete(@Nonnull final Long id) {
    mailLogRepository.deleteById(id);
  }

  @Nonnull
  @Override
  @Transactional
  public MailLogDto logSent(@Nonnull final Email mail,
                            @Nonnull final String providerMessageId) {
    final var recipients = mail.getRecipients();
    final var recipientEmail = recipients.isEmpty() ? Publ.EMPTY_STRING
            : recipients.getFirst().getAddress();
    final var recipientName = recipients.isEmpty() ? Publ.EMPTY_STRING
            : Objects.requireNonNullElse(recipients.getFirst().getName(), Publ.EMPTY_STRING);

    final var subject = Objects.requireNonNullElse(mail.getSubject(), Publ.EMPTY_STRING);
    final var content = mail.getHTMLText() != null ? mail.getHTMLText()
            : Objects.requireNonNullElse(mail.getPlainText(), Publ.EMPTY_STRING);

    return logSent(
            recipientEmail,
            recipientName,
            Publ.EMPTY_STRING,
            subject,
            content,
            null,
            providerMessageId,
            null
    );
  }

  @Nonnull
  @Override
  @Transactional
  public MailLogDto logSent(@Nonnull final String recipientEmail,
                            @Nonnull final String recipientName,
                            @Nonnull final String templateName,
                            @Nonnull final String subject,
                            @Nonnull final String content,
                            @Nullable final String placeholdersJson,
                            @Nonnull final String providerMessageId,
                            final Long createdBy) {
    final var dto = new MailLogDto();
    dto.setRecipientEmail(recipientEmail);
    dto.setRecipientName(recipientName);
    dto.setTemplateName(templateName);
    dto.setSubject(subject);
    dto.setContent(content);
    dto.setPlaceholdersJson(placeholdersJson);
    dto.setStatus(MailLogStatus.SENT);
    dto.setErrorMessage(Publ.EMPTY_STRING);
    dto.setProviderMessageId(providerMessageId);
    dto.setSentAt(LocalDateTime.now());
    dto.setCreatedAt(LocalDateTime.now());
    dto.setCreatedBy(createdBy);
    return create(dto);
  }

  @Override
  @Transactional
  public MailLogDto logFailed(@Nonnull final Email mail,
                              @Nonnull final String errorMessage) {
    final var recipients = mail.getRecipients();
    final var recipientEmail = recipients.isEmpty() ? Publ.EMPTY_STRING
            : recipients.getFirst().getAddress();
    final var recipientName = recipients.isEmpty() ? Publ.EMPTY_STRING
            : Objects.requireNonNullElse(recipients.getFirst().getName(), Publ.EMPTY_STRING);

    final var subject = Objects.requireNonNullElse(mail.getSubject(), Publ.EMPTY_STRING);
    final var content = mail.getHTMLText() != null ? mail.getHTMLText()
            : Objects.requireNonNullElse(mail.getPlainText(), Publ.EMPTY_STRING);

    return logFailed(
            recipientEmail,
            recipientName,
            Publ.EMPTY_STRING,
            subject,
            content,
            null,
            errorMessage,
            null
    );
  }

  @Nonnull
  @Override
  @Transactional
  public MailLogDto logFailed(@Nonnull final String recipientEmail,
                              @Nonnull final String recipientName,
                              @Nonnull final String templateName,
                              @Nonnull final String subject,
                              @Nonnull final String content,
                              @Nullable final String placeholdersJson,
                              @Nonnull final String errorMessage,
                              final Long createdBy) {
    final var dto = new MailLogDto();
    dto.setRecipientEmail(recipientEmail);
    dto.setRecipientName(recipientName);
    dto.setTemplateName(templateName);
    dto.setSubject(subject);
    dto.setContent(content);
    dto.setPlaceholdersJson(placeholdersJson);
    dto.setStatus(MailLogStatus.FAILED);
    dto.setErrorMessage(errorMessage);
    dto.setProviderMessageId(Publ.EMPTY_STRING);
    dto.setCreatedAt(LocalDateTime.now());
    dto.setCreatedBy(createdBy);
    return create(dto);
  }

  @Nonnull
  @Override
  public List<MailLogDto> findMailLogs(@Nonnull final MailLogFilter filter,
                                       final int offset,
                                       final int limit,
                                       @Nonnull final List<QuerySortOrder> sortOrders) {
    final var spec = mailLogSpec.getSpecification(filter);
    final var pageable = PageHelper.createPageRequest(offset, limit, sortOrders);

    return mailLogRepository.findAll(spec, pageable).stream()
            .map(MailLogMapper::toDto)
            .toList();
  }
}
