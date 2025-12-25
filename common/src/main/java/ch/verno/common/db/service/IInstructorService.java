package ch.verno.common.db.service;

import ch.verno.common.db.dto.InstructorDto;
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
}
