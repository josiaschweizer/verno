package ch.verno.server.report.course;

import ch.verno.contract.dto.file.temp.FileDto;
import ch.verno.contract.dto.table.course.CourseDto;
import ch.verno.contract.dto.table.file.FileDownload;
import ch.verno.contract.dto.table.participant.ParticipantDto;
import ch.verno.lib.Lazy;
import ch.verno.lib.Publ;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.bo.BoFactory;
import ch.verno.server.bo.file.StorageBo;
import ch.verno.server.bo.table.setting.TenantSettingBo;
import ch.verno.server.service.intern.table.participant.ParticipantService;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class CourseReportUseCase {

  @Nonnull private final Lazy<StorageBo> storageBo;
  @Nonnull private final Lazy<TenantSettingBo> tenantSettingBo;
  @Nonnull private final Lazy<ParticipantService> participantService;
  @Nonnull private final Lazy<CourseReportRenderer> courseReportRenderer;

  public CourseReportUseCase(@Nonnull final ServerBean serverBean) {
    this.storageBo = Lazy.of(() -> serverBean.get(BoFactory.class).get(StorageBo.class));
    this.tenantSettingBo = Lazy.of(() -> serverBean.get(TenantSettingBo.class));
    this.participantService = Lazy.of(() -> serverBean.get(ParticipantService.class));
    this.courseReportRenderer = Lazy.of(() -> serverBean.get(CourseReportRenderer.class));
  }

  @Nonnull
  public FileDto generate(@Nonnull final CourseDto course) {
    final var participants = participantService.get().findParticipantsByCourse(course);
    return generate(course, participants);
  }

  @Nonnull
  public FileDto generate(@Nonnull final CourseDto course,
                          @Nonnull final List<ParticipantDto> participants) {
    final var courseDates = new ArrayList<LocalDate>(); // TODO aus Schedules ableiten
    final var reportData = CourseReportMapper.map(course, participants, courseDates);

    final var settings = tenantSettingBo.get().getCurrentOrDefaultTenantSetting();

    byte[] templateBytes = null;
    if (settings.getCourseReportTemplate() != null) {
      final var templateDownload = storageBo.get().download(settings.getCourseReportTemplate());
      templateBytes = getTemplateBytes(templateDownload);
    }

    final var pdfBytes = courseReportRenderer.get().renderReportPdf(reportData, templateBytes);
    final var filename = settings.getCourseReportName().toLowerCase() + Publ.UNDERSCORE + course.getTitle().toLowerCase() + ".pdf";
    return new FileDto(filename, pdfBytes);
  }

  @Nonnull
  private byte[] getTemplateBytes(@Nonnull final FileDownload templateDownload) {
    try (final var inputStream = templateDownload.stream()) {
      if (inputStream != null) {
        return inputStream.readAllBytes();
      } else {
        throw new RuntimeException("Template file not found");
      }
    } catch (Exception e) {
      throw new RuntimeException("Failed to load template file", e);
    }
  }
}