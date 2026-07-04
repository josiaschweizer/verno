package ch.verno.contract.endpoint.instructor;

import ch.verno.contract.dto.filter.InstructorFilter;
import ch.verno.contract.dto.response.base.delete.DeleteResponse;
import ch.verno.contract.dto.response.base.save.SaveResponse;
import ch.verno.contract.dto.table.base.SortOrderDto;
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
  List<InstructorDto> getInstructors(@Nonnull InstructorFilter filter,
                                     @Nonnull List<SortOrderDto> sortOrders,
                                     int offset,
                                     int limit);

  @Nonnull
  InstructorDto saveInstructor(@Nonnull InstructorDto instructor);

  @Nonnull
  DeleteResponse deleteInstructor(@Nonnull InstructorDto instructor);

  @Nonnull
  DeleteResponse deleteInstructorById(@Nonnull Long id);

  boolean isInstructorReferenced(@Nonnull Long instructorId);

  @Nonnull
  SaveResponse<InstructorDto> apiSaveInstructor(@Nonnull InstructorDto instructorDto);


}
