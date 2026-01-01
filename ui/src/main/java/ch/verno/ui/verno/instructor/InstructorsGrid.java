package ch.verno.ui.verno.instructor;

import ch.verno.common.db.dto.InstructorDto;
import ch.verno.common.db.filter.InstructorFilter;
import ch.verno.common.util.VernoConstants;
import ch.verno.server.service.InstructorService;
import ch.verno.ui.base.grid.BaseOverviewGrid;
import ch.verno.ui.lib.Routes;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.Nonnull;
import jakarta.annotation.security.PermitAll;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

@PermitAll
@Route(Routes.INSTRUCTORS)
@PageTitle("Instructors Overview")
@Menu(order = 2, icon = "vaadin:institution", title = "Instructors Overview")
public class InstructorsGrid extends BaseOverviewGrid<InstructorDto, InstructorFilter> {

  @Nonnull
  private final InstructorService instructorService;

  public InstructorsGrid(@Nonnull final InstructorService instructorService) {
    super(InstructorFilter.empty());
    this.instructorService = instructorService;
  }

  @Nonnull
  @Override
  protected Stream<InstructorDto> fetch(@Nonnull final Query<InstructorDto, InstructorFilter> query, @Nonnull final InstructorFilter filter) {
    final var offset = query.getOffset();
    final var limit = query.getLimit();
    final var sortOrders = query.getSortOrders();

    return instructorService.findInstructors(filter, offset, limit, sortOrders).stream();
  }

  @Override
  protected int count(@Nonnull final Query<InstructorDto, InstructorFilter> query,
                      @Nonnull final InstructorFilter filter) {
    return instructorService.countCourses(filter);
  }

  @Nonnull
  @Override
  protected String getGridObjectName() {
    return VernoConstants.INSTRUCTOR;
  }

  @Nonnull
  @Override
  protected String getDetailPageRoute() {
    return Routes.createUrlFromUrlSegments(Routes.INSTRUCTORS, Routes.DETAIL);
  }

  @Nonnull
  @Override
  protected Map<ValueProvider<InstructorDto, Object>, String> getColumns() {
    final var columnsMap = new LinkedHashMap<ValueProvider<InstructorDto, Object>, String>();
    columnsMap.put(InstructorDto::getFirstName, getTranslation("shared.first.name"));
    columnsMap.put(InstructorDto::getLastName, getTranslation("shared.last.name"));
    columnsMap.put(InstructorDto::genderAsString, getTranslation("gender"));
    columnsMap.put(InstructorDto::getEmail, getTranslation("shared.e.mail"));
    columnsMap.put(InstructorDto::phoneAsString, getTranslation("shared.phone"));
    columnsMap.put((dto) -> dto.getAddress().getFullAddressAsString(), getTranslation("shared.address"));
    return columnsMap;
  }

  @Nonnull
  @Override
  protected InstructorFilter withSearchText(@Nonnull final String searchText) {
    return InstructorFilter.ofSearchText(searchText);
  }
}
