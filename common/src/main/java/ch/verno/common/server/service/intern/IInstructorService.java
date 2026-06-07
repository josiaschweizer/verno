package ch.verno.common.server.service.intern;

import ch.verno.common.db.dto.table.InstructorDto;
import ch.verno.common.db.filter.InstructorFilter;
import com.vaadin.flow.data.provider.QuerySortOrder;
import jakarta.annotation.Nonnull;

import java.util.List;

public interface IInstructorService {

  @Nonnull
  InstructorDto createInstructor(@Nonnull final InstructorDto instructorDto);

  @Nonnull
  InstructorDto updateInstructor(@Nonnull final InstructorDto instructorDto);

  @Nonnull
  InstructorDto getInstructorById(@Nonnull final Long id);

  @Nonnull
  List<InstructorDto> getAllInstructors();

  void deleteInstructor(@Nonnull final InstructorDto instructorDto);

  void deleteInstructor(@Nonnull final Long id);

  boolean isInstructorReferenced(@Nonnull Long id);

  @Nonnull
  List<InstructorDto> findInstructors(@Nonnull InstructorFilter filter,
                                      int offset,
                                      int limit,
                                      @Nonnull List<QuerySortOrder> sortOrders);

  int countCourses(@Nonnull InstructorFilter filter);
}
