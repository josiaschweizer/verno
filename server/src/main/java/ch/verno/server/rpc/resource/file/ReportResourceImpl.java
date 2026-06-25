package ch.verno.server.rpc.resource.file;

import ch.verno.contract.dto.table.course.CourseDto;
import ch.verno.contract.dto.table.participant.ParticipantDto;
import ch.verno.contract.endpoint.file.ReportResource;
import ch.verno.contract.rpc.RpcResource;
import ch.verno.server.bean.ServerBean;
import jakarta.annotation.Nonnull;

import java.util.List;

@RpcResource(ReportResource.class)
public class ReportResourceImpl implements ReportResource {

  public ReportResourceImpl(@Nonnull final ServerBean serverBean){

  }

  @Nonnull
  @Override
  public String generateCourseReport(@Nonnull final CourseDto course,
                                     @Nonnull final List<ParticipantDto> participants) {
    return "";
  }
}
