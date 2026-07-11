package ch.verno.server.service.entity.mail;

import ch.verno.common.type.mail.MailLogStatus;
import ch.verno.contract.dto.filter.MailLogFilter;
import ch.verno.contract.dto.table.mail.MailLogDto;
import ch.verno.db.entity.mail.MailLogEntity;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.mapper.mail.MailLogMapper;
import ch.verno.server.repository.mail.MailLogRepository;
import ch.verno.server.service.base.AbstractSpecEntityService;
import ch.verno.server.spec.MailLogSpec;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MailLogService extends AbstractSpecEntityService<
        Long,
        MailLogEntity,
        MailLogDto,
        MailLogRepository,
        MailLogMapper,
        MailLogSpec,
        MailLogFilter> {

  public MailLogService(@Nonnull final ServerBean serverBean) {
    super(serverBean.get(MailLogRepository.class), serverBean.get(MailLogMapper.class), MailLogSpec::new);
  }

  @Nonnull
  public List<MailLogDto> findAllByStatus(@Nonnull final MailLogStatus status) {
    return getRepository().findAllByStatus(status)
            .stream()
            .map(getMapper()::toDto)
            .toList();
  }

  @Nonnull
  public List<MailLogDto> findAllByRecipientEmail(@Nonnull final String recipientEmail) {
    return getRepository().findAllByRecipientEmail(recipientEmail)
            .stream()
            .map(getMapper()::toDto)
            .toList();
  }

  @Nonnull
  public MailLogDto saveUnscoped(@Nonnull final MailLogDto dto) {
    MailLogEntity entity = getMapper().toNewEntity(dto);
    entity = getRepository().saveUnscoped(entity);
    return getMapper().toDto(entity);
  }

}
