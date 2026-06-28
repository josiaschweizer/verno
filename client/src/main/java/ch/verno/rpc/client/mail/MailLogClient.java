package ch.verno.rpc.client.mail;

import ch.verno.contract.dto.filter.MailLogFilter;
import ch.verno.contract.dto.table.mail.MailLogDto;
import ch.verno.contract.endpoint.mail.MailLogResource;
import ch.verno.lib.Lazy;
import ch.verno.rpc.client.helper.SortOrderMapper;
import ch.verno.rpc.rpc.RpcFactory;
import com.google.inject.Inject;
import com.vaadin.flow.data.provider.QuerySortOrder;
import jakarta.annotation.Nonnull;

import java.util.List;

public class MailLogClient {

  @Nonnull private final Lazy<MailLogResource> mailLogResource;

  @Inject
  public MailLogClient(@Nonnull final RpcFactory rpcFactory) {
    this.mailLogResource = Lazy.of(() -> rpcFactory.create(MailLogResource.class));
  }

  @Nonnull
  public List<MailLogDto> getMailLogs(@Nonnull final MailLogFilter filter,
                                      final int offset,
                                      final int limit,
                                      @Nonnull final List<QuerySortOrder> sortOrders) {
    final var orders = SortOrderMapper.toDto(sortOrders);
    return mailLogResource.get().findMailLogs(filter, offset, limit, orders);
  }

}
