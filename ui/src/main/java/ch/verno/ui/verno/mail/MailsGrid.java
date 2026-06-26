package ch.verno.ui.verno.mail;

import ch.verno.common.dto.ui.badge.VABadgeLabelOptions;
import ch.verno.common.lib.Routes;
import ch.verno.common.lib.format.Converter;
import ch.verno.common.type.mail.MailValidity;
import ch.verno.contract.dto.filter.MailLogFilter;
import ch.verno.contract.dto.table.mail.MailConfigDto;
import ch.verno.contract.dto.table.mail.MailLogDto;
import ch.verno.lib.Lazy;
import ch.verno.lib.New;
import ch.verno.lib.Publ;
import ch.verno.lib.VernoUtility;
import ch.verno.rpc.client.mail.MailConfigClient;
import ch.verno.rpc.client.mail.MailLogClient;
import ch.verno.ui.base.components.badge.VABadgeLabel;
import ch.verno.ui.base.components.button.ButtonBuilder;
import ch.verno.ui.base.components.emptystate.VAEmptyState;
import ch.verno.ui.base.components.notification.inline.VAInlineNotification;
import ch.verno.ui.lib.icon.IconUtil;
import ch.verno.ui.lib.pages.grid.BaseOverviewGrid;
import ch.verno.ui.lib.pages.grid.ComponentGridColumn;
import ch.verno.ui.lib.pages.grid.ObjectGridColumn;
import ch.verno.ui.verno.mail.testmail.TestMailTemplatePage;
import com.google.inject.Injector;
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
import org.jetbrains.annotations.NonNls;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

@PermitAll
@Route(Routes.MAIL_LOG)
@Menu(order = 96, icon = "vaadin:mailbox", title = "mail.log")
public class MailsGrid extends BaseOverviewGrid<MailLogDto, MailLogFilter> implements BeforeEnterObserver {

  @NonNls public static final String GRID_COLUMN_RECIPIENT_EMAIL = "recipient-email";
  @NonNls public static final String GRID_COLUMN_RECIPIENT_NAME = "recipient-name";
  @NonNls public static final String GRID_COLUMN_SUBJECT = "subject";
  @NonNls public static final String GRID_COLUMN_SENT_AT = "sent-at";
  @NonNls public static final String GRID_COLUMN_ERROR_MESSAGE = "error-message";
  @NonNls public static final String GRID_COLUMN_STATUS = "status";

  @Nonnull private final Lazy<MailLogClient> mailLogClient;
  @Nonnull private final Lazy<MailConfigClient> mailConfigClient;

  @Autowired
  protected MailsGrid(@Nonnull final Injector injector) {
    super(injector, MailLogFilter.empty());

    this.mailLogClient = Lazy.of(() -> injector.getInstance(MailLogClient.class));
    this.mailConfigClient = Lazy.of(() -> injector.getInstance(MailConfigClient.class));
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
      if (!mailConfigClient.get().hasMailConfigForCurrentTenant()) {
        return false;
      }

      final var mailConfig = mailConfigClient.get().getMailConfigForCurrentTenant().orElseGet(MailConfigDto::empty);
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

    return mailLogClient.get().getMailLogs(filter, offset, limit, sortOrders).stream();
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

    columns.add(new ObjectGridColumn<>(GRID_COLUMN_RECIPIENT_EMAIL, MailLogDto::getRecipientEmail, getTranslation("mail.recipient.email"), true));
    columns.add(new ObjectGridColumn<>(GRID_COLUMN_RECIPIENT_NAME, MailLogDto::getRecipientName, getTranslation("mail.recipient.name"), true));
    columns.add(new ObjectGridColumn<>(GRID_COLUMN_SUBJECT, MailLogDto::getSubject, getTranslation("mail.subject"), true));
    columns.add(new ObjectGridColumn<>(GRID_COLUMN_SENT_AT, mailLogDto -> Converter.localDateTime(mailLogDto.getSentAt()), getTranslation("mail.sent.at"), true));
    columns.add(new ObjectGridColumn<>(GRID_COLUMN_ERROR_MESSAGE, MailLogDto::getErrorMessage, getTranslation("base.error"), false));

    return columns;
  }

  @Nonnull
  @Override
  protected List<ComponentGridColumn<MailLogDto>> getComponentColumns() {
    final var columns = new ArrayList<ComponentGridColumn<MailLogDto>>();

    columns.add(new ComponentGridColumn<>(GRID_COLUMN_STATUS, this::getBadgeLabel, getTranslation("shared.status"), true));

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
    notification.setDescription("Test your Email Texts and your Email Configuration."); //TODO translation
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
