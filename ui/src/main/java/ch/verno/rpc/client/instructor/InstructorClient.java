package ch.verno.rpc.client.instructor;

import ch.verno.contract.dto.table.instructor.InstructorDto;
import ch.verno.contract.endpoint.instructor.InstructorResource;
import ch.verno.lib.Lazy;
import ch.verno.rpc.rpc.RpcFactory;
import com.google.inject.Inject;
import jakarta.annotation.Nonnull;

import java.util.List;
import java.util.Optional;

public class InstructorClient {

  @Nonnull private final Lazy<InstructorResource> instructorResource;

  @Inject
  public InstructorClient(@Nonnull final RpcFactory rpcFactory) {
    this.instructorResource = Lazy.of(() -> rpcFactory.create(InstructorResource.class));
  }

  @Nonnull
  public Optional<InstructorDto> getInstructorById(@Nonnull final Long id) {
    return instructorResource.get().getInstructorById(id);
  }

  @Nonnull
  public List<InstructorDto> getAllInstructors() {
    return instructorResource.get().getAllInstructors();
  }

  @Nonnull
  @SuppressWarnings("UnusedReturnValue")
  public InstructorDto saveInstructor(@Nonnull final InstructorDto instructor) {
    return instructorResource.get().saveInstructor(instructor);
  }

}
