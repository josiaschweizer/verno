package ch.verno.ui.feature.importing.csv;

import ch.verno.contract.dto.table.instructor.InstructorDto;
import com.google.inject.Inject;
import com.google.inject.Injector;
import jakarta.annotation.Nonnull;

public class InstructorCsvMapper extends AbstractCsvMapper<InstructorDto> {

  @Inject
  public InstructorCsvMapper(@Nonnull final Injector injector) {
    super(injector);
  }

  @Nonnull
  @Override
  protected InstructorDto newTarget() {
    return InstructorDto.empty();
  }

}
