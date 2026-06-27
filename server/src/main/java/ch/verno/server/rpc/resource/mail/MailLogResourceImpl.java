package ch.verno.server.rpc.resource.mail;

import ch.verno.contract.dto.filter.MailLogFilter;
import ch.verno.contract.dto.table.base.SortOrderDto;
import ch.verno.contract.dto.table.mail.MailLogDto;
import ch.verno.contract.endpoint.mail.MailLogResource;
import ch.verno.contract.rpc.RpcResource;
import ch.verno.lib.Lazy;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.service.intern.table.mail.MailLogService;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RpcResource(MailLogResource.class)
public class MailLogResourceImpl implements MailLogResource {

  @Nonnull private final Lazy<MailLogService> mailLogService;

  public MailLogResourceImpl(@Nonnull final ServerBean serverBean) {
    this.mailLogService = Lazy.of(() -> serverBean.get(MailLogService.class));
  }

  @Nonnull
  @Override
  public List<MailLogDto> findMailLogs(@Nonnull final MailLogFilter filter,
                                       final int offset,
                                       final int limit,
                                       @Nonnull final List<SortOrderDto> sortOrders) {
    return mailLogService.get().findAll(filter, sortOrders, offset, limit);
  }
}
