package ch.verno.ui.verno.instructor;

import ch.verno.common.db.dto.InstructorDto;
import ch.verno.common.util.VernoConstants;
import ch.verno.server.service.InstructorService;
import ch.verno.ui.base.grid.BaseOverviewGrid;
import ch.verno.ui.lib.Routes;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.ItemDoubleClickEvent;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.Nonnull;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Route(Routes.INSTRUCTORS)
@PageTitle("Instructors Overview")
@Menu(order = 2, icon = "vaadin:academy-cap", title = "Instructors Overview")
public class InstructorsGrid extends BaseOverviewGrid<InstructorDto> {

  @Nonnull
  private final InstructorService instructorService;

  public InstructorsGrid(@Nonnull final InstructorService instructorService) {
    this.instructorService = instructorService;

    initGrid();
  }

  @Override
  protected void onGridItemDoubleClick(@Nonnull final ItemDoubleClickEvent<InstructorDto> event) {
    final var url = Routes.getDetailURL(this.getClass());
    final var redirectURL = Routes.getURLWithId(url, event.getItem().getId());
    UI.getCurrent().navigate(redirectURL);
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
    return columnsMap;
  }
}
