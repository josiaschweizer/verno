package ch.verno.ui.verno.dashboard.io.widgets.instructor;

import ch.verno.common.base.components.entry.phonenumber.PhoneNumber;
import ch.verno.common.db.dto.AddressDto;
import ch.verno.common.db.dto.InstructorDto;
import ch.verno.common.db.service.IInstructorService;
import ch.verno.common.file.CsvMapDto;
import ch.verno.common.file.FileServerGate;
import ch.verno.common.gate.VernoApplicationGate;
import ch.verno.publ.Publ;
import ch.verno.server.io.importing.dto.DbField;
import ch.verno.server.io.importing.dto.DbFieldNested;
import ch.verno.server.io.importing.dto.DbFieldTyped;
import ch.verno.server.mapper.csv.InstructorCsvMapper;
import ch.verno.server.service.AddressService;
import ch.verno.ui.verno.dashboard.io.widgets.ImportEntityConfig;
import ch.verno.ui.verno.dashboard.io.widgets.ImportResult;
import jakarta.annotation.Nonnull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Import configuration for Instructor entities.
 */
public class InstructorImportConfig implements ImportEntityConfig<InstructorDto> {

  @Nonnull
  private final VernoApplicationGate vernoApplicationGate;

  public InstructorImportConfig(@Nonnull final VernoApplicationGate vernoApplicationGate) {
    this.vernoApplicationGate = vernoApplicationGate;
  }

  @Nonnull
  @Override
  public List<DbField<InstructorDto>> getDbFields() {
    return List.of(
            new DbField<>("firstname", "shared.first.name", InstructorDto::setFirstName, true),
            new DbField<>("lastname", "shared.last.name", InstructorDto::setLastName, true),
            new DbField<>("email", "shared.e.mail", InstructorDto::setEmail, true)
    );
  }

  @Override
  public List<DbFieldTyped<InstructorDto, ?>> getTypedDbFields() {
    return List.of(
            new DbFieldTyped<>(
                    "phone",
                    "shared.telefon",
                    PhoneNumber::fromString,
                    InstructorDto::setPhone,
                    false
            )
    );
  }

  @Override
  public List<DbFieldNested<InstructorDto, ?>> getNestedDbFields() {
    return List.of(
            new DbFieldNested<>(
                    "address",
                    "shared.address",
                    AddressDto::new,
                    InstructorDto::setAddress,
                    List.of(
                            new DbField<>("street", "shared.street", AddressDto::setStreet, false),
                            new DbField<>("houseNumber", "shared.house.number", AddressDto::setHouseNumber, false),
                            new DbField<>("zipCode", "shared.zip.code", AddressDto::setZipCode, false),
                            new DbField<>("city", "shared.city", AddressDto::setCity, false),
                            new DbField<>("country", "shared.country", AddressDto::setCountry, false)
                    ),
                    List.of(),
                    false
            )
    );
  }

  @Nonnull
  @Override
  public ImportResult performImport(@Nonnull final String fileToken,
                                    @Nonnull final Map<String, String> mapping) {

    final var fileServerGate = vernoApplicationGate.getService(FileServerGate.class);
    final var fileDto = fileServerGate.loadFile(fileToken);
    final var csvRows = fileServerGate.parseRows(fileDto);

    final var mapper = new InstructorCsvMapper();
    final var result = mapper.map(
            csvRows,
            mapping,
            getDbFields(),
            getTypedDbFields(),
            getNestedDbFields()
    );

    final var saveables = result.saveables();
    final var instructorService = vernoApplicationGate.getService(IInstructorService.class);

    for (final var saveable : saveables) {
      processNestedEntities(saveable);
      instructorService.createInstructor(saveable);
    }

    if (!result.errors().isEmpty()) {
      final var errorCsvRows = new ArrayList<CsvMapDto>();
      for (final var error : result.errors()) {
        final var csvRow = csvRows.get(error.rowIndex() - 1);
        csvRow.row().put("import_error", String.join(Publ.COMMA, error.message()));
        errorCsvRows.add(csvRow);
      }

      final var errorFile = fileServerGate.parseRows(errorCsvRows, "instructor_import_errors.csv");
      final var token = fileServerGate.store(errorFile);
      return ImportResult.partialSuccess(token, errorFile.filename());
    }

    return ImportResult.completeSuccessInstance();
  }

  private void processNestedEntities(@Nonnull final InstructorDto instructor) {
    final var addressService = vernoApplicationGate.getService(AddressService.class);

    if (!instructor.getAddress().isEmpty()) {
      final var addressDto = addressService.findOrCreateAddress(instructor.getAddress());
      instructor.setAddress(addressDto);
    }
  }
}