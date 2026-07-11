package ch.verno.contract.endpoint.mail;

import ch.verno.contract.dto.filter.MailLogFilter;
import ch.verno.contract.dto.table.base.SortOrderDto;
import ch.verno.contract.dto.table.mail.MailLogDto;
import ch.verno.contract.rpc.RpcEndpoint;
import jakarta.annotation.Nonnull;

import java.util.List;

@RpcEndpoint
public interface MailLogResource {

  @Nonnull
  List<MailLogDto> findMailLogs(@Nonnull MailLogFilter filter,
                                int offset,
                                int limit,
                                @Nonnull List<SortOrderDto> sortOrders);

}
