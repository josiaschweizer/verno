package ch.verno.server.rpc.resource.instructor;

import ch.verno.contract.dto.filter.InstructorFilter;
import ch.verno.contract.dto.response.base.delete.DeleteResponse;
import ch.verno.contract.dto.response.base.save.SaveResponse;
import ch.verno.contract.dto.table.base.SortOrderDto;
import ch.verno.contract.dto.table.instructor.InstructorDto;
import ch.verno.contract.endpoint.instructor.InstructorResource;
import ch.verno.contract.rpc.RpcResource;
import ch.verno.lib.Lazy;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.bo.BoFactory;
import ch.verno.server.bo.table.instructor.InstructorBo;
import ch.verno.server.service.intern.table.instructor.InstructorService;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RpcResource(InstructorResource.class)
public class InstructorResourceImpl implements InstructorResource {

  @Nonnull private final Lazy<InstructorBo> instructorBo;
  @Nonnull private final Lazy<InstructorService> instructorService;

  public InstructorResourceImpl(@Nonnull final ServerBean serverBean) {
    this.instructorBo = Lazy.of(() -> BoFactory.getInstance(serverBean).get(InstructorBo.class));
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
  public List<InstructorDto> getInstructors(@Nonnull final InstructorFilter filter,
                                            @Nonnull final List<SortOrderDto> sortOrders, final int offset,
                                            final int limit) {
    return instructorService.get().findAll(filter, sortOrders, offset, limit);
  }

  @Nonnull
  @Override
  public SaveResponse<InstructorDto> saveInstructor(@Nonnull final InstructorDto instructor) {
    return instructorBo.get().saveInstructor(instructor);
  }

  @Nonnull
  @Override
  public DeleteResponse deleteInstructor(@Nonnull final InstructorDto instructor) {
    return instructorService.get().delete(instructor);
  }

  @Nonnull
  @Override
  public DeleteResponse deleteInstructorById(@Nonnull final Long id) {
    return instructorService.get().deleteById(id);
  }

  @Override
  public boolean isInstructorReferenced(@Nonnull final Long instructorId) {
    return instructorBo.get().isInstructorReferenced(instructorId);
  }
}
