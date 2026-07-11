package ch.verno.ui.verno.dashboard.course;

import ch.verno.common.type.billing.BillingLicenceOption;
import ch.verno.common.type.mail.MailValidity;
import ch.verno.contract.dto.filter.ParticipantFilter;
import ch.verno.contract.dto.table.course.CourseDto;
import ch.verno.contract.dto.table.participant.ParticipantDto;
import ch.verno.contract.mail.MailTemplateType;
import ch.verno.lib.Lazy;
import ch.verno.lib.Publ;
import ch.verno.lib.VernoConstants;
import ch.verno.lib.exception.ExceptionUtil;
import ch.verno.rpc.client.billing.BillingClient;
import ch.verno.rpc.client.course.CourseClient;
import ch.verno.rpc.client.mail.MailConfigClient;
import ch.verno.rpc.client.participant.ParticipantClient;
import ch.verno.ui.base.components.contextmenu.ActionDef;
import ch.verno.ui.base.components.widget.VAAccordionWidgetBase;
import ch.verno.ui.base.factory.SpanFactory;
import ch.verno.ui.verno.dashboard.assignment.AssignToCourseDialog;
import ch.verno.ui.verno.dashboard.mail.CourseMailDialog;
import ch.verno.ui.verno.dashboard.report.CourseReportDialog;
import ch.verno.ui.verno.participant.ParticipantsGrid;
import com.google.inject.Injector;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.data.provider.Query;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class CourseWidget extends VAAccordionWidgetBase {

  @Nonnull private final Injector injector;

  @Nonnull private final Lazy<BillingClient> billingClient;
  @Nonnull private final Lazy<MailConfigClient> mailConfigService;
  @Nonnull private final Lazy<ParticipantClient> participantClient;

  @Nonnull private final CourseDto currentCourse;
  @Nullable private ParticipantsGrid participantsGrid;
  @Nonnull private List<ParticipantDto> participantsInCourse;

  public CourseWidget(@Nonnull final Injector injector,
                      @Nonnull final Long currentCourseId) {
    this.injector = injector;

    this.billingClient = Lazy.of(()->injector.getInstance(BillingClient.class));
    this.mailConfigService = Lazy.of(() -> injector.getInstance(MailConfigClient.class));
    this.participantClient = Lazy.of(() -> injector.getInstance(ParticipantClient.class));


    final var courseService = injector.getInstance(CourseClient.class);
    currentCourse = courseService.getCourseById(currentCourseId).orElseThrow(ExceptionUtil::toEntityNotFoundException);

    final var participantClient = injector.getInstance(ParticipantClient.class);
    participantsInCourse = participantClient.getParticipants(ParticipantFilter.fromCourseId(currentCourse.getId() != null ? Set.of(currentCourse.getId()) : null));

    buildUI();
  }

  @Nonnull
  @Override
  protected String getTitleText() {
    return currentCourse.getTitle();
  }

  @Override
  protected void buildHeaderActions(@Nonnull final HorizontalLayout header) {
    final var emailButton = createHeaderButton(
            getTranslation("setting.send.email"),
            VaadinIcon.MAILBOX,
            e -> {
              final var dialog = new CourseMailDialog(injector, MailTemplateType.COURSE_INVITE);
              dialog.setParticipants(participantsInCourse);
              dialog.setCourse(currentCourse);
              dialog.open();
            }
    );

    final var mailConfig = mailConfigService.get().getMailConfigForCurrentTenant();
    if (mailConfig.isPresent()) {
      if (mailConfig.get().getMailValidity().equals(MailValidity.TESTED_VALID) && !participantsInCourse.isEmpty()) {
        emailButton.removePseudoEnabled();
      } else {
        String tooltipText;
        if (participantsInCourse.isEmpty()) {
          tooltipText = getTranslation("shared.no.participants.assigned.to.this.course.please.assign.participants.to.enable.this.feature");
        } else if (participantsInCourse.size() > VernoConstants.MAX_MAIL_BATCH_SIZE) {
          tooltipText = getTranslation("shared.verno.cannot.proccess.more.than.0.emails.at.once.please.reduce.the.number.of.participants.in.this.course.to.enable.this.feature.for.more.information.please.contact.support", VernoConstants.MAX_MAIL_BATCH_SIZE);
        } else {
          tooltipText = getTranslation("setting.your.email.configuration.is.not.valid.please.check.your.settings");
        }

        emailButton.setPseudoEnabled(false, tooltipText);
      }
    } else {
      emailButton.setPseudoEnabled(false, getTranslation("setting.please.set.up.your.email.configuration.in.the.settings.to.enable.this.feature"));
    }

    final var reportButton = createHeaderButton(
            getTranslation("setting.report"),
            VaadinIcon.FILE_TEXT,
            e -> new CourseReportDialog(
                    injector,
                    currentCourse,
                    participantsInCourse)
                    .open()
    );
    if (!billingClient.get().isTenantBillingOptionLicenced(BillingLicenceOption.REPORT)) {
      reportButton.setPseudoEnabled(false, getTranslation("shared.the.report.option.is.not.licensed.for.your.tenant.please.contact.your.tenant.administrator.to.enable.this.feature"));
    }

    final var assignButton = createHeaderButton(getTranslation("participant.edit.participant"),
            VaadinIcon.COG, e -> {
              final var dialog = new AssignToCourseDialog(injector, currentCourse);
              dialog.addClosedListener(ev -> refresh());
              dialog.addDialogCloseActionListener(ev -> refresh());
              dialog.open();
            });

    final var detailButton = createHeaderButton(Publ.EMPTY_STRING,
            VaadinIcon.EXTERNAL_LINK, e -> {
              final var courseDetailDialog = new CourseDetailDialog(injector, currentCourse);
              courseDetailDialog.open();
            });

    header.add(emailButton, reportButton, assignButton, detailButton);
  }

  @Override
  protected void initContent() {
    if (!participantsInCourse.isEmpty()) {
      this.participantsGrid = new ParticipantsGrid(injector, false, false) {

        @Nonnull
        @Override
        protected Stream<ParticipantDto> fetch(@Nonnull final Query<ParticipantDto, ParticipantFilter> query,
                                               @Nonnull final ParticipantFilter filter) {
          if (currentCourse.getId() != null) {
            filter.setCourseIds(Set.of(currentCourse.getId()));
          }

          final var participants = super.fetch(query, filter).toList();
          CourseWidget.this.participantsInCourse = participants;
          return participants.stream();
        }

        @Nonnull
        @Override
        protected List<ActionDef> buildContextMenuActions(@Nonnull final ParticipantDto dto) {
          final var actions = new ArrayList<ActionDef>();
          actions.add(ActionDef.create(
                  SpanFactory.createSpan(getTranslation("participant.remove.participant.from.course"), VaadinIcon.TRASH),
                  () -> removeParticipant(dto)
          ));
          return actions;
        }
      };

      participantsGrid.getGrid().setAllRowsVisible(true);
      add(participantsGrid);
    } else {
      add(new Text(getTranslation("shared.no.participants.assigned.to.this.course.yet")));
    }
  }

  private void removeParticipant(@Nonnull final ParticipantDto dto) {
    participantClient.get().removeCourse(dto.getId(), currentCourse);
    refresh();
  }

  @Override
  protected void refresh() {
    final var oldParticipantsInCourse = participantsInCourse;

    if (participantsGrid == null) {
      participantsInCourse = participantClient.get().getParticipants(ParticipantFilter.fromCourseId(currentCourse.getId() != null ? Set.of(currentCourse.getId()) : null));
    } else {
      participantsGrid.setFilter(participantsGrid.getFilter());
      participantsInCourse = participantsGrid.getGrid()
              .getDataProvider()
              .fetch(new Query<>())
              .toList();
    }


    if ((oldParticipantsInCourse.isEmpty() && !participantsInCourse.isEmpty()) ||
            (!oldParticipantsInCourse.isEmpty() && participantsInCourse.isEmpty())) {
      buildUI();
    }
  }
}