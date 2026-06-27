package ch.verno.ui.verno.dashboard.io.widgets.instructor;

import ch.verno.contract.dto.file.temp.CsvMapDto;
import ch.verno.contract.dto.table.instructor.InstructorDto;
import ch.verno.lib.Lazy;
import ch.verno.lib.Publ;
import ch.verno.rpc.client.instructor.InstructorClient;
import ch.verno.ui.i18n.AbstractTranslationHelper;
import ch.verno.ui.verno.dashboard.io.widgets.ExportEntityConfig;
import com.google.inject.Inject;
import com.google.inject.Injector;
import jakarta.annotation.Nonnull;

import java.util.LinkedHashMap;
import java.util.List;

public class InstructorExportConfig extends AbstractTranslationHelper implements ExportEntityConfig<InstructorDto> {

  @Nonnull private final Lazy<InstructorClient> instructorClient;

  @Inject
  public InstructorExportConfig(@Nonnull final Injector injector) {
    super(injector);
    this.instructorClient = Lazy.of(() -> injector.getInstance(InstructorClient.class));
  }

  @Nonnull
  @Override
  public List<CsvMapDto> getRows() {
    final var instructors = instructorClient.get().getAllInstructors();

    return instructors.stream()
            .map(this::instructorToCsvMap)
            .toList();
  }

  @Nonnull
  private CsvMapDto instructorToCsvMap(@Nonnull final InstructorDto instructor) {
    final var row = new LinkedHashMap<String, String>();

    row.put(getTranslation( "shared.id"), instructor.getId() != null ? instructor.getId().toString() : Publ.EMPTY_STRING);
    row.put(getTranslation( "shared.first.name"), instructor.getFirstName());
    row.put(getTranslation( "shared.last.name"), instructor.getLastName());
    row.put(getTranslation( "shared.e.mail"), instructor.getEmail());
    row.put(getTranslation( "shared.telefon"), instructor.phoneAsString());
    row.put(getTranslation( "shared.gender"), instructor.genderAsString());
    row.put(getTranslation( "shared.street"), instructor.getAddress().getStreet());
    row.put(getTranslation( "shared.house.number"), instructor.getAddress().getHouseNumber());
    row.put(getTranslation( "shared.zip.code"), instructor.getAddress().getZipCode());
    row.put(getTranslation( "course.location"), instructor.getAddress().getCity());
    row.put(getTranslation( "shared.country"), instructor.getAddress().getCountry());

    return new CsvMapDto(row);
  }

  @Nonnull
  @Override
  public String getFileName() {
    return "instructors.csv";
  }
}
