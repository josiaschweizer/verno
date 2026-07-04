package ch.verno.rpc.client.instructor;

import ch.verno.contract.dto.filter.InstructorFilter;
import ch.verno.contract.dto.response.base.save.SaveResponse;
import ch.verno.contract.dto.table.instructor.InstructorDto;
import ch.verno.contract.endpoint.instructor.InstructorResource;
import ch.verno.lib.Lazy;
import ch.verno.rpc.client.helper.SortOrderMapper;
import ch.verno.rpc.rpc.RpcFactory;
import com.google.inject.Inject;
import com.vaadin.flow.data.provider.QuerySortOrder;
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
  public List<InstructorDto> getInstructors(@Nonnull final InstructorFilter filter,
                                            @Nonnull final List<QuerySortOrder> sortOrders,
                                            final int offset,
                                            final int limit) {
    final var orders = SortOrderMapper.toDto(sortOrders);
    return instructorResource.get().getInstructors(filter, orders, offset, limit);
  }

  @Nonnull
  public InstructorDto saveInstructor(@Nonnull final InstructorDto instructor) {
    return instructorResource.get().saveInstructor(instructor);
  }

  public void deleteInstructor(@Nonnull final InstructorDto instructor) {
    instructorResource.get().deleteInstructor(instructor);
  }

  public boolean isInstructorReferenced(@Nonnull final Long instructorId) {
    return instructorResource.get().isInstructorReferenced(instructorId);
  }

  @Nonnull
  public SaveResponse<InstructorDto> apiSaveInstructor(@Nonnull final InstructorDto instructor) {
    return instructorResource.get().apiSaveInstructor(instructor);
  }

}
