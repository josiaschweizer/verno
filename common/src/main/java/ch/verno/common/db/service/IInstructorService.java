package ch.verno.common.db.service;

import ch.verno.common.db.dto.InstructorDto;
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

  @Nonnull
  List<InstructorDto> findInstructors(@Nonnull InstructorFilter filter,
                                      int offset,
                                      int limit,
                                      @Nonnull List<QuerySortOrder> sortOrders);

  int countCourses(@Nonnull InstructorFilter filter);
}
