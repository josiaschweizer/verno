package ch.verno.server.report.course;

import ch.verno.common.db.dto.CourseDto;
import ch.verno.common.db.dto.ParticipantDto;
import ch.verno.common.db.service.IMandantSettingService;
import ch.verno.common.db.service.IParticipantService;
import ch.verno.common.report.ReportDto;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class CourseReportUseCase {

  @Nonnull private final IParticipantService participantService;
  @Nonnull private final IMandantSettingService mandantSettingService;
  @Nonnull private final CourseReportRenderer courseReportRenderer;

  public CourseReportUseCase(@Nonnull final IParticipantService participantService,
                             @Nonnull final IMandantSettingService mandantSettingService,
                             @Nonnull final CourseReportRenderer courseReportRenderer) {
    this.participantService = participantService;
    this.mandantSettingService = mandantSettingService;
    this.courseReportRenderer = courseReportRenderer;
  }

  @Nonnull
  public ReportDto generate(@Nonnull final CourseDto course) {
    final var participants = participantService.findParticipantsByCourse(course);
    return generate(course, participants);
  }

  @Nonnull
  public ReportDto generate(@Nonnull final CourseDto course,
                            @Nonnull final List<ParticipantDto> participants) {
    final var courseDates = new ArrayList<LocalDate>(); //todo liste ergänzen -> auflösen nach course & course schedule
    final var reportData = CourseReportMapper.map(course, participants, courseDates);


    final var prefix = mandantSettingService.getSingleMandantSetting().getCourseReportName();
    final var filename = prefix + course.getTitle() + ".pdf";
    final var pdfBytes = courseReportRenderer.renderReportPdf(reportData);
    return new ReportDto(filename, pdfBytes);
  }

}
