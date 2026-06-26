package ch.verno.rpc.client.file;

import ch.verno.contract.dto.table.course.CourseDto;
import ch.verno.contract.dto.table.participant.ParticipantDto;
import ch.verno.contract.endpoint.file.ReportResource;
import ch.verno.lib.Lazy;
import ch.verno.rpc.rpc.RpcFactory;
import com.google.inject.Inject;
import jakarta.annotation.Nonnull;

import java.util.List;

public class ReportClient {

  @Nonnull private final Lazy<ReportResource> reportResource;

  @Inject
  public ReportClient(@Nonnull final RpcFactory rpcFactory) {
    this.reportResource = Lazy.of(() -> rpcFactory.create(ReportResource.class));
  }

  @Nonnull
  public String generateCourseReport(@Nonnull CourseDto course,
                                     @Nonnull List<ParticipantDto> participants){
    return reportResource.get().generateCourseReport(course, participants);
  }

  @Nonnull
  public String generateParticipantsReport() {
    return reportResource.get().generateParticipantsReport();
  }

}
