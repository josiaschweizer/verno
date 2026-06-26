package ch.verno.server.rpc.resource.file;

import ch.verno.contract.dto.table.course.CourseDto;
import ch.verno.contract.dto.table.participant.ParticipantDto;
import ch.verno.contract.endpoint.file.ReportResource;
import ch.verno.contract.rpc.RpcResource;
import ch.verno.lib.Lazy;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.bo.file.TempFileBo;
import ch.verno.server.report.course.CourseReportUseCase;
import ch.verno.server.report.participant.ParticipantReportUseCase;
import jakarta.annotation.Nonnull;

import java.util.List;

@RpcResource(ReportResource.class)
public class ReportResourceImpl implements ReportResource {

  @Nonnull private final Lazy<TempFileBo> tempFileBo;
  @Nonnull private final Lazy<CourseReportUseCase> courseReportUseCase;
  @Nonnull private final Lazy<ParticipantReportUseCase> participantReportUseCase;

  public ReportResourceImpl(@Nonnull final ServerBean serverBean){
    this.tempFileBo = Lazy.of(() -> serverBean.get(TempFileBo.class));
    this.courseReportUseCase = Lazy.of(() -> serverBean.get(CourseReportUseCase.class));
    this.participantReportUseCase = Lazy.of(() -> serverBean.get(ParticipantReportUseCase.class));
  }

  @Nonnull
  @Override
  public String generateCourseReport(@Nonnull final CourseDto course,
                                     @Nonnull final List<ParticipantDto> participants) {
    final var report = courseReportUseCase.get().generate(course, participants);
    return tempFileBo.get().store(report.filename(), report.pdfBytes());
  }

  @Nonnull
  @Override
  public String generateParticipantsReport() {
    final var report = participantReportUseCase.get().generate();
    return tempFileBo.get().store(report.filename(), report.pdfBytes());
  }
}
