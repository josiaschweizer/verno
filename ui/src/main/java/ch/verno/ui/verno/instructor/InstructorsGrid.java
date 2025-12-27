package ch.verno.ui.verno.instructor;

import ch.verno.common.db.dto.InstructorDto;
import ch.verno.common.util.VernoConstants;
import ch.verno.server.service.InstructorService;
import ch.verno.ui.base.grid.BaseOverviewGrid;
import ch.verno.ui.lib.Routes;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.Nonnull;
import jakarta.annotation.security.PermitAll;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@PermitAll
@Route(Routes.INSTRUCTORS)
@PageTitle("Instructors Overview")
@Menu(order = 2, icon = "vaadin:institution", title = "Instructors Overview")
public class InstructorsGrid extends BaseOverviewGrid<InstructorDto> {

  @Nonnull
  private final InstructorService instructorService;

  public InstructorsGrid(@Nonnull final InstructorService instructorService) {
    this.instructorService = instructorService;
  }

  @Nonnull
  @Override
  protected List<InstructorDto> fetchItems() {
    return instructorService.getAllInstructors();
  }

  @Nonnull
  @Override
  protected String getGridObjectName() {
    return VernoConstants.INSTRUCTOR;
  }

  @Nonnull
  @Override
  protected Map<ValueProvider<InstructorDto, Object>, String> getColumns() {
    final var columnsMap = new LinkedHashMap<ValueProvider<InstructorDto, Object>, String>();
    columnsMap.put(InstructorDto::getFirstName, "First Name");
    columnsMap.put(InstructorDto::getLastName, "Last Name");
    columnsMap.put(InstructorDto::genderAsString, "Gender");
    columnsMap.put(InstructorDto::getEmail, "Email");
    columnsMap.put(InstructorDto::phoneAsString, "Phone");
    columnsMap.put((dto) -> dto.getAddress().getFullAddressAsString(), "Address");
    return columnsMap;
  }
}
