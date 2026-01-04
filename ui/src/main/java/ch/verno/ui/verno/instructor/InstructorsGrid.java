package ch.verno.ui.verno.instructor;

import ch.verno.common.db.dto.InstructorDto;
import ch.verno.common.db.filter.InstructorFilter;
import ch.verno.server.service.InstructorService;
import ch.verno.ui.base.grid.BaseOverviewGrid;
import ch.verno.ui.base.grid.ObjectGridColumn;
import ch.verno.ui.lib.Routes;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;
import jakarta.annotation.Nonnull;
import jakarta.annotation.security.PermitAll;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@PermitAll
@Route(Routes.INSTRUCTORS)
@Menu(order = 2, icon = "vaadin:institution", title = "shared.instructors.overview")
public class InstructorsGrid extends BaseOverviewGrid<InstructorDto, InstructorFilter> implements HasDynamicTitle {

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
    return getTranslation("shared.instructor");
  }

  @Nonnull
  @Override
  protected String getDetailPageRoute() {
    return Routes.createUrlFromUrlSegments(Routes.INSTRUCTORS, Routes.DETAIL);
  }

  @Nonnull
  @Override
  protected List<ObjectGridColumn<InstructorDto>> getColumns() {
    final var columns = new ArrayList<ObjectGridColumn<InstructorDto>>();
    columns.add(new ObjectGridColumn<>("firstname", InstructorDto::getFirstName, getTranslation("shared.first.name"), true));
    columns.add(new ObjectGridColumn<>("lastname", InstructorDto::getLastName, getTranslation("shared.last.name"), true));
    columns.add(new ObjectGridColumn<>("gender", InstructorDto::genderAsString, getTranslation("shared.gender"), true));
    columns.add(new ObjectGridColumn<>("email", InstructorDto::getEmail, getTranslation("shared.e.mail"), true));
    columns.add(new ObjectGridColumn<>("phone", InstructorDto::phoneAsString, getTranslation("shared.phone"), true));
    columns.add(new ObjectGridColumn<>("address", (dto) -> dto.getAddress().getFullAddressAsString(), getTranslation("shared.address"), true));
    return columns;
  }

  @Nonnull
  @Override
  protected InstructorFilter withSearchText(@Nonnull final String searchText) {
    return InstructorFilter.ofSearchText(searchText);
  }

  @Override
  public String getPageTitle() {
    return getTranslation("shared.instructors.overview");
  }
}
