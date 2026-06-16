package ch.verno.ui.verno.mail;

import ch.verno.common.db.dto.table.mail.MailLogDto;
import ch.verno.common.db.filter.MailLogFilter;
import ch.verno.common.db.type.mail.MailValidity;
import ch.verno.common.gate.GlobalInterface;
import ch.verno.common.lib.format.Converter;
import ch.verno.common.server.service.intern.mail.IMailConfigService;
import ch.verno.common.server.service.intern.mail.IMailLogService;
import ch.verno.common.ui.base.components.badge.VABadgeLabelOptions;
import ch.verno.lib.New;
import ch.verno.publ.Publ;
import ch.verno.publ.Routes;
import ch.verno.publ.VernoUtility;
import ch.verno.server.service.intern.mail.MailLogService;
import ch.verno.ui.base.components.badge.VABadgeLabel;
import ch.verno.ui.base.components.button.ButtonBuilder;
import ch.verno.ui.base.components.emptystate.VAEmptyState;
import ch.verno.ui.base.components.notification.inline.VAInlineNotification;
import ch.verno.ui.lib.pages.grid.BaseOverviewGrid;
import ch.verno.ui.lib.pages.grid.ComponentGridColumn;
import ch.verno.ui.lib.pages.grid.ObjectGridColumn;
import ch.verno.ui.lib.icon.IconUtil;
import ch.verno.ui.verno.mail.testmail.TestMailTemplatePage;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.GridSortOrder;
import com.vaadin.flow.component.grid.ItemDoubleClickEvent;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;
import jakarta.annotation.Nonnull;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

@PermitAll
@Route(Routes.MAIL_LOG)
@Menu(order = 96, icon = "vaadin:mailbox", title = "mail.log")
public class MailsGrid extends BaseOverviewGrid<MailLogDto, MailLogFilter> implements BeforeEnterObserver {

  @Nonnull private final IMailLogService mailLogService;
  @Nonnull private final IMailConfigService mailConfigService;

  @Autowired
  protected MailsGrid(@Nonnull final GlobalInterface globalInterface) {
    super(globalInterface, MailLogFilter.empty());

    this.mailLogService = globalInterface.getService(MailLogService.class);
    this.mailConfigService = globalInterface.getService(IMailConfigService.class);
  }

  @Override
  public void beforeEnter(@Nonnull final BeforeEnterEvent event) {
    // Redirect to settings if mail configuration is not valid
    if (!hasValidMailConfiguration()) {
      event.forwardTo(Routes.TENANT_SETTINGS);
    }
  }

  private boolean hasValidMailConfiguration() {
    try {
      if (!mailConfigService.hasConfigForCurrentTenant()) {
        return false;
      }

      final var mailConfig = mailConfigService.getConfigForCurrentTenant();
      return mailConfig.getMailValidity() == MailValidity.TESTED_VALID;
    } catch (Exception e) {
      return false;
    }
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
    final var columns = new ArrayList<ObjectGridColumn<MailLogDto>>();

    columns.add(new ObjectGridColumn<>("recipientEmail", MailLogDto::getRecipientEmail, getTranslation("mail.recipient.email"), true));
    columns.add(new ObjectGridColumn<>("recipientName", MailLogDto::getRecipientName, getTranslation("mail.recipient.name"), true));
    columns.add(new ObjectGridColumn<>("subject", MailLogDto::getSubject, getTranslation("mail.subject"), true));
    columns.add(new ObjectGridColumn<>("sentAt", mailLogDto -> Converter.localDateTime(mailLogDto.getSentAt()), getTranslation("mail.sent.at"), true));
    columns.add(new ObjectGridColumn<>("errorMessage", MailLogDto::getErrorMessage, getTranslation("base.error"), false));

    return columns;
  }

  @Nonnull
  @Override
  protected List<ComponentGridColumn<MailLogDto>> getComponentColumns() {
    final var columns = new ArrayList<ComponentGridColumn<MailLogDto>>();

    columns.add(new ComponentGridColumn<>("status", this::getBadgeLabel, "Status", true));

    return columns;
  }

  @Nonnull
  private VABadgeLabel getBadgeLabel(@Nonnull final MailLogDto mailLog) {
    final var badgeDisplayName = getTranslation(mailLog.getStatus().getDisplayKey());

    return switch (mailLog.getStatus()) {
      case SENT -> new VABadgeLabel(badgeDisplayName, VABadgeLabelOptions.SUCCESS);
      case FAILED -> new VABadgeLabel(badgeDisplayName, VABadgeLabelOptions.ERROR);
      case QUEUED -> new VABadgeLabel(badgeDisplayName, VABadgeLabelOptions.CONTRAST);
    };
  }

  @Nonnull
  @Override
  protected MailLogFilter withSearchText(@Nonnull final String searchText) {
    return MailLogFilter.ofSearchText(searchText);
  }

  @Override
  protected boolean hasDetailPage() {
    return false;
  }

  @Override
  protected void onGridItemDoubleClick(@Nonnull final ItemDoubleClickEvent<MailLogDto> event) {
    // override the default behavior of re-navigating to the detail view on double click
  }

  @Nonnull
  @Override
  protected HashMap<Integer, Component> getCustomComponents() {
    final var map = New.<Integer, Component>hashMap();
    map.put(Publ.ONE, getEmailTestLayout());
    return map;
  }

  @Nonnull
  private VerticalLayout getEmailTestLayout() {
    final var button = ButtonBuilder.iconVerticalView(
            IconUtil.create(VaadinIcon.EXTERNAL_LINK),
            "External Link to Mail Configuration",
            TestMailTemplatePage.class
    );

    final var notification = new VAInlineNotification();
    notification.setActionAlignment(VAInlineNotification.VAInlineNotificationActionAlignment.RIGHT_CENTERED);
    notification.setTitle("Test Emails");
    notification.setDescription("Test your Email Texts and your Email Configuration.");
    notification.setActions(button);

    final var layout = new VerticalLayout(notification);
    layout.getStyle().setPaddingTop(VernoUtility.LUMO_SPACE_XS);
    layout.getStyle().setPaddingRight(VernoUtility.LUMO_SPACE_XS);
    layout.getStyle().setPaddingBottom(VernoUtility.LUMO_SPACE_NONE);
    layout.getStyle().setPaddingLeft(VernoUtility.LUMO_SPACE_XS);
    return layout;
  }

  @Nonnull
  @Override
  protected VAEmptyState createEmptyState() {
    final var emptyState = super.createEmptyState();
    emptyState.removeDescriptions();
    return emptyState;
  }

  @Override
  protected void setDefaultSorting() {
    final var sentAtCol = columnsByKey.get("sentAt");
    if (sentAtCol == null) {
      return;
    }

    grid.sort(List.of(new GridSortOrder<>(sentAtCol, SortDirection.DESCENDING)));
  }
}
