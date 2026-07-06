package ch.verno.contract.endpoint.file;

import ch.verno.contract.dto.table.course.CourseDto;
import ch.verno.contract.dto.table.participant.ParticipantDto;
import ch.verno.contract.rpc.RpcEndpoint;
import jakarta.annotation.Nonnull;

import java.util.List;

@RpcEndpoint
public interface ReportResource {

  @Nonnull
  String generateCourseReport(@Nonnull CourseDto course, @Nonnull List<ParticipantDto> participants);

  @Nonnull
  String generateParticipantsReport();

  void deleteTempFile(@Nonnull String token);

}
