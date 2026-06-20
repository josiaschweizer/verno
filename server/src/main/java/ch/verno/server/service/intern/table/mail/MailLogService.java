package ch.verno.server.service.intern.table.mail;

import ch.verno.common.type.mail.MailLogStatus;
import ch.verno.contract.dto.table.mail.MailLogDto;
import ch.verno.db.entity.mail.MailLogEntity;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.mapper.mail.MailLogMapper;
import ch.verno.server.repository.mail.MailLogRepository;
import ch.verno.server.service.base.AbstractEntityService;
import jakarta.annotation.Nonnull;

import java.util.List;

public class MailLogService extends AbstractEntityService<
        MailLogEntity,
        MailLogDto,
        MailLogRepository,
        MailLogMapper> {

  public MailLogService(@Nonnull final ServerBean serverBean) {
    super(serverBean.get(MailLogRepository.class), serverBean.get(MailLogMapper.class));
  }

  @Nonnull
  @Override
  public MailLogDto create(@Nonnull final MailLogDto dto) {
    return super.create(dto);
  }

  @Nonnull
  public List<MailLogDto> findAllByStatus(@Nonnull final MailLogStatus status) {
    return getRepository().findAllByStatus(status)
            .stream()
            .map(getMapper()::toSimpleDto)
            .toList();
  }

  @Nonnull
  public List<MailLogDto> findAllByRecipientEmail(@Nonnull final String recipientEmail) {
    return getRepository().findAllByRecipientEmail(recipientEmail)
            .stream()
            .map(getMapper()::toSimpleDto)
            .toList();
  }

}
