package ch.verno.ui.verno.dashboard.io.widgets.instructor;

import ch.verno.common.api.dto.internal.file.temp.CsvMapDto;
import ch.verno.common.db.dto.table.AddressDto;
import ch.verno.common.db.dto.table.InstructorDto;
import ch.verno.common.db.service.intern.IInstructorService;
import ch.verno.common.gate.GlobalInterface;
import ch.verno.common.gate.server.TempFileServerGate;
import ch.verno.common.lib.i18n.TranslationHelper;
import ch.verno.common.ui.base.components.entry.phonenumber.PhoneNumber;
import ch.verno.server.io.importing.dto.DbField;
import ch.verno.server.io.importing.dto.DbFieldNested;
import ch.verno.server.io.importing.dto.DbFieldTyped;
import ch.verno.server.mapper.csv.CsvMappingRowError;
import ch.verno.server.mapper.csv.InstructorCsvMapper;
import ch.verno.server.service.intern.AddressService;
import ch.verno.ui.verno.dashboard.io.widgets.ImportEntityConfig;
import ch.verno.ui.verno.dashboard.io.widgets.ImportResult;
import jakarta.annotation.Nonnull;
import org.jetbrains.annotations.NonNls;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InstructorImportConfig implements ImportEntityConfig<InstructorDto> {

  @NonNls public static final String FIRSTNAME = "firstname";
  @NonNls public static final String LASTNAME = "lastname";
  @NonNls public static final String EMAIL = "email";
  @NonNls public static final String PHONE = "phone";
  @NonNls public static final String ADDRESS = "address";
  @NonNls public static final String STREET = "street";
  @NonNls public static final String ZIP_CODE = "zip-code";
  @NonNls public static final String HOUSE_NUMBER = "house-number";
  @NonNls public static final String CITY = "city";
  @NonNls public static final String COUNTRY = "country";

  @NonNls public static final String ERROR_UK_INSTRUCTOR_MANDANT_EMAIL = "uk_instructor_mandant_email";

  @Nonnull private final GlobalInterface globalInterface;

  public InstructorImportConfig(@Nonnull final GlobalInterface globalInterface) {
    this.globalInterface = globalInterface;
  }

  @Nonnull
  @Override
  public List<DbField<InstructorDto>> getDbFields() {
    return List.of(
            new DbField<>(FIRSTNAME, "shared.first.name", InstructorDto::setFirstName, true),
            new DbField<>(LASTNAME, "shared.last.name", InstructorDto::setLastName, true),
            new DbField<>(EMAIL, "shared.e.mail", InstructorDto::setEmail, true)
    );
  }

  @Nonnull
  @Override
  public List<DbFieldTyped<InstructorDto, ?>> getTypedDbFields() {
    return List.of(
            new DbFieldTyped<>(
                    PHONE,
                    "shared.telefon",
                    PhoneNumber::fromString,
                    InstructorDto::setPhone,
                    false
            )
    );
  }

  @Nonnull
  @Override
  public List<DbFieldNested<InstructorDto, ?>> getNestedDbFields() {
    final var address = new DbFieldNested<>(
            ADDRESS,
            "shared.address",
            AddressDto::new,
            InstructorDto::setAddress,
            List.of(
                    new DbField<>(STREET, "shared.street", AddressDto::setStreet, false),
                    new DbField<>(HOUSE_NUMBER, "shared.house.number", AddressDto::setHouseNumber, false),
                    new DbField<>(ZIP_CODE, "shared.zip.code", AddressDto::setZipCode, false),
                    new DbField<>(CITY, "shared.city", AddressDto::setCity, false),
                    new DbField<>(COUNTRY, "shared.country", AddressDto::setCountry, false)
            ),
            List.of(),
            false
    );

    return List.of(address);
  }

  @Nonnull
  @Override
  public ImportResult performImport(@Nonnull final String fileToken,
                                    @Nonnull final Map<String, String> mapping) {

    final var fileServerGate = globalInterface.getService(TempFileServerGate.class);
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
    final var instructorService = globalInterface.getService(IInstructorService.class);

    final var importErrors = new ArrayList<>(result.errors());
    for (int i = 0; i < saveables.size(); i++) {
      final var saveable = saveables.get(i);
      processNestedEntities(saveable);

      try {
        instructorService.createInstructor(saveable);
      } catch (DataIntegrityViolationException e) {
        importErrors.add(new CsvMappingRowError(
                i + 1,
                buildImportErrorMessage(saveable, e)
        ));
      } catch (Exception e) {
        importErrors.add(new CsvMappingRowError(
                i + 1,
                TranslationHelper.getTranslation(globalInterface, "common.unerwarteter.fehler.beim.import.0", e.getMessage())
        ));
      }
    }

    if (!importErrors.isEmpty()) {
      final var errorCsvRows = new ArrayList<CsvMapDto>();
      for (final var error : importErrors) {
        final var csvRow = csvRows.get(error.rowIndex() - 1);
        csvRow.row().put(getImportErrorColumnName(), error.message());
        errorCsvRows.add(csvRow);
      }

      final var errorFile = fileServerGate.parseRows(errorCsvRows, "instructor_import_errors.csv");
      final var token = fileServerGate.store(errorFile);

      return ImportResult.partialSuccess(token, errorFile.filename());
    }

    return ImportResult.completeSuccessInstance();
  }

  @Nonnull
  private String buildImportErrorMessage(@Nonnull final InstructorDto instructor,
                                         @Nonnull final DataIntegrityViolationException e) {
    final var message = e.getMostSpecificCause().getMessage();
    if (message != null && message.contains(ERROR_UK_INSTRUCTOR_MANDANT_EMAIL)) {
      return TranslationHelper.getTranslation(globalInterface, "common.instructor.mit.dieser.e.mail.existiert.bereits.0", instructor.getEmail());
    }

    return TranslationHelper.getTranslation(globalInterface, "common.datenbankfehler.beim.import.0", message);
  }

  private void processNestedEntities(@Nonnull final InstructorDto instructor) {
    final var addressService = globalInterface.getService(AddressService.class);

    if (!instructor.getAddress().isEmpty()) {
      final var addressDto = addressService.findOrCreateAddress(instructor.getAddress());
      instructor.setAddress(addressDto);
    }
  }
}