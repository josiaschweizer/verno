package ch.verno.ui.verno.dashboard.io.widgets.instructor;

import ch.verno.common.db.dto.table.InstructorDto;
import ch.verno.common.db.service.IInstructorService;
import ch.verno.common.file.dto.CsvMapDto;
import ch.verno.common.gate.GlobalInterface;
import ch.verno.common.lib.i18n.AbstractTranslationHelper;
import ch.verno.publ.Publ;
import ch.verno.ui.verno.dashboard.io.widgets.ExportEntityConfig;
import jakarta.annotation.Nonnull;

import java.util.LinkedHashMap;
import java.util.List;

public class InstructorExportConfig extends AbstractTranslationHelper implements ExportEntityConfig<InstructorDto> {

  @Nonnull private final GlobalInterface globalInterface;

  public InstructorExportConfig(@Nonnull final GlobalInterface globalInterface) {
    this.globalInterface = globalInterface;
  }

  @Nonnull
  @Override
  public List<CsvMapDto> getRows() {
    final var instructorService = globalInterface.getService(IInstructorService.class);
    final var instructors = instructorService.getAllInstructors();

    return instructors.stream()
            .map(this::instructorToCsvMap)
            .toList();
  }

  @Nonnull
  private CsvMapDto instructorToCsvMap(@Nonnull final InstructorDto instructor) {
    final var row = new LinkedHashMap<String, String>();

    row.put(getTranslation(globalInterface, "shared.id"), instructor.getId() != null ? instructor.getId().toString() : Publ.EMPTY_STRING);
    row.put(getTranslation(globalInterface, "shared.first.name"), instructor.getFirstName());
    row.put(getTranslation(globalInterface, "shared.last.name"), instructor.getLastName());
    row.put(getTranslation(globalInterface, "shared.e.mail"), instructor.getEmail());
    row.put(getTranslation(globalInterface, "shared.telefon"), instructor.phoneAsString());
    row.put(getTranslation(globalInterface, "shared.gender"), instructor.genderAsString());
    row.put(getTranslation(globalInterface, "shared.street"), instructor.getAddress().getStreet());
    row.put(getTranslation(globalInterface, "shared.house.number"), instructor.getAddress().getHouseNumber());
    row.put(getTranslation(globalInterface, "shared.zip.code"), instructor.getAddress().getZipCode());
    row.put(getTranslation(globalInterface, "course.location"), instructor.getAddress().getCity());
    row.put(getTranslation(globalInterface, "shared.country"), instructor.getAddress().getCountry());

    return new CsvMapDto(row);
  }

  @Nonnull
  @Override
  public String getFileName() {
    return "instructors.csv";
  }
}
