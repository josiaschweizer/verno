package ch.verno.contract.endpoint.instructor;

import ch.verno.contract.dto.result.base.SaveResult;
import ch.verno.contract.dto.table.instructor.InstructorDto;
import ch.verno.contract.rpc.RpcEndpoint;
import jakarta.annotation.Nonnull;

import java.util.List;
import java.util.Optional;

@RpcEndpoint
public interface InstructorResource {

  @Nonnull
  Optional<InstructorDto> getInstructorById(@Nonnull final Long id);

  @Nonnull
  List<InstructorDto> getAllInstructors();

  @Nonnull
  SaveResult<InstructorDto> saveInstructor(@Nonnull InstructorDto instructor);

}
