package ch.verno.ui.verno.mail;

import ch.verno.common.db.dto.table.mail.MailLogDto;
import ch.verno.common.db.filter.MailLogFilter;
import ch.verno.common.db.service.mail.IMailLogService;
import ch.verno.common.gate.GlobalInterface;
import ch.verno.publ.Routes;
import ch.verno.server.service.intern.mail.MailLogService;
import ch.verno.ui.base.pages.grid.BaseOverviewGrid;
import ch.verno.ui.base.pages.grid.ObjectGridColumn;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;
import jakarta.annotation.Nonnull;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Stream;

@PermitAll
@Route(Routes.MAIL_LOG)
@Menu(order = 96, icon = "vaadin:mailbox", title = "mail.log")
public class MailsGrid extends BaseOverviewGrid<MailLogDto, MailLogFilter> implements HasDynamicTitle {

  @Nonnull private final IMailLogService mailLogService;

  @Autowired
  protected MailsGrid(@Nonnull final GlobalInterface globalInterface) {
    super(globalInterface, MailLogFilter.empty());

    this.mailLogService = globalInterface.getService(MailLogService.class);
  }

  @Override
  protected void initGrid() {
    super.initGrid();
  }

  @Nonnull
  @Override
  protected Stream<MailLogDto> fetch(@Nonnull final Query<MailLogDto, MailLogFilter> query,
                                     @Nonnull final MailLogFilter filter) {
    final var offset = query.getOffset();
    final var limit = query.getLimit();
    final var sortOrders = query.getSortOrders();

    return mailLogService.findMailLogs(filter, offset, limit, sortOrders).stream();
  }

  @Nonnull
  @Override
  protected String getGridObjectName() {
    return getTranslation("mail.mail");
  }

  @Nonnull
  @Override
  protected List<ObjectGridColumn<MailLogDto>> getColumns() {
    return List.of();
  }

  @Override
  public String getPageTitle() {
    return "";
  }
}
