package ch.verno.server.rpc.resource.instructor;

import ch.verno.contract.dto.table.instructor.InstructorDto;
import ch.verno.contract.endpoint.instructor.InstructorResource;
import ch.verno.contract.rpc.RpcResource;
import ch.verno.lib.Lazy;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.service.intern.table.instructor.InstructorService;
import jakarta.annotation.Nonnull;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
@RpcResource(InstructorResource.class)
public class InstructorResourceImpl implements InstructorResource {

  @Nonnull private final Lazy<InstructorService> instructorService;

  public InstructorResourceImpl(@Nonnull final ServerBean serverBean) {
    this.instructorService = Lazy.of(() -> serverBean.get(InstructorService.class));
  }

  @Nonnull
  @Override
  public Optional<InstructorDto> getInstructorById(@Nonnull final Long id) {
    return instructorService.get().findById(id);
  }

  @Nonnull
  @Override
  public List<InstructorDto> getAllInstructors() {
    return instructorService.get().findAll();
  }

  @Nonnull
  @Override
  public InstructorDto saveInstructor(@Nonnull final InstructorDto instructor) {
    return instructorService.get().save(instructor);
  }
}
