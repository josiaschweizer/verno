package ch.verno.ui.verno.dashboard.io.widgets.participant;

import ch.verno.common.api.dto.internal.file.temp.CsvMapDto;
import ch.verno.common.db.dto.table.AddressDto;
import ch.verno.common.db.dto.table.ParentDto;
import ch.verno.common.db.dto.table.ParticipantDto;
import ch.verno.common.server.service.intern.ICourseLevelService;
import ch.verno.common.server.service.intern.IGenderService;
import ch.verno.common.server.service.intern.IParticipantService;
import ch.verno.common.gate.GlobalInterface;
import ch.verno.common.gate.server.TempFileServerGate;
import ch.verno.ui.i18n.TranslationHelper;
import ch.verno.common.ui.base.components.entry.phonenumber.PhoneNumber;
import ch.verno.lib.New;
import ch.verno.server.io.importing.dto.DbField;
import ch.verno.server.io.importing.dto.DbFieldNested;
import ch.verno.server.io.importing.dto.DbFieldRelation;
import ch.verno.server.io.importing.dto.DbFieldTyped;
import ch.verno.server.mapper.csv.CsvMappingRowError;
import ch.verno.server.mapper.csv.ParticipantCsvMapper;
import ch.verno.server.service.intern.AddressService;
import ch.verno.server.service.intern.ParentService;
import ch.verno.ui.verno.dashboard.io.widgets.ImportEntityConfig;
import ch.verno.ui.verno.dashboard.io.widgets.ImportResult;
import jakarta.annotation.Nonnull;
import org.jetbrains.annotations.NonNls;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ParticipantImportConfig implements ImportEntityConfig<ParticipantDto> {

  @NonNls public static final String FIRSTNAME = "firstname";
  @NonNls public static final String LASTNAME = "lastname";
  @NonNls public static final String EMAIL = "email";
  @NonNls public static final String NOTE = "note";
  @NonNls public static final String BIRTHDATE = "birthdate";
  @NonNls public static final String PHONE = "phone";
  @NonNls public static final String ADDRESS = "address";
  @NonNls public static final String STREET = "street";
  @NonNls public static final String HOUSE_NUMBER = "house-number";
  @NonNls public static final String ZIP_CODE = "zip-code";
  @NonNls public static final String CITY = "city";
  @NonNls public static final String COUNTRY = "country";
  @NonNls public static final String PARENT_ONE = "parent-one";
  @NonNls public static final String PARENT_TWO = "parent-two";
  @NonNls public static final String COURSE_LEVEL = "course-level";
  @NonNls public static final String GENDER = "gender";

  @NonNls public static final String ERROR_DUPLICATE_KEY_VALUE_VIOLATES_UNIQUE_CONSTRAINT = "duplicate key value violates unique constraint";

  @Nonnull private final GlobalInterface globalInterface;
  @Nonnull private final ICourseLevelService courseLevelService;
  @Nonnull private final IGenderService genderService;

  public ParticipantImportConfig(@Nonnull final GlobalInterface globalInterface) {
    this.globalInterface = globalInterface;
    this.courseLevelService = globalInterface.getService(ICourseLevelService.class);
    this.genderService = globalInterface.getService(IGenderService.class);
  }

  @Nonnull
  @Override
  public List<DbField<ParticipantDto>> getDbFields() {
    return List.of(
            new DbField<>(FIRSTNAME, "shared.first.name", ParticipantDto::setFirstName, true),
            new DbField<>(LASTNAME, "shared.last.name", ParticipantDto::setLastName, true),
            new DbField<>(EMAIL, "shared.e.mail", ParticipantDto::setEmail, true),
            new DbField<>(NOTE, "shared.note", ParticipantDto::setNote, false)
    );
  }

  @Nonnull
  @Override
  public List<DbFieldTyped<ParticipantDto, ?>> getTypedDbFields() {
    return List.of(
            new DbFieldTyped<>(
                    BIRTHDATE,
                    "shared.birthdate",
                    ParticipantImportParser::parseDate,
                    ParticipantDto::setBirthdate,
                    false
            ),
            new DbFieldTyped<>(
                    PHONE,
                    "shared.telefon",
                    PhoneNumber::fromString,
                    ParticipantDto::setPhone,
                    false
            )
    );
  }

  @Nonnull
  @Override
  public List<DbFieldNested<ParticipantDto, ?>> getNestedDbFields() {
    final var address = new DbFieldNested<>(
            ADDRESS,
            "shared.address",
            AddressDto::new,
            ParticipantDto::setAddress,
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
    final var parentOne = new DbFieldNested<>(
            PARENT_ONE,
            "participant.parent_one",
            ParentDto::new,
            ParticipantDto::setParentOne,
            List.of(
                    new DbField<>(FIRSTNAME, "shared.first.name", ParentDto::setFirstName, false),
                    new DbField<>(LASTNAME, "shared.last.name", ParentDto::setLastName, false),
                    new DbField<>(EMAIL, "shared.e.mail", ParentDto::setEmail, false)
            ),
            List.of(
                    new DbFieldTyped<>(
                            PHONE,
                            "shared.telefon",
                            PhoneNumber::fromString,
                            ParentDto::setPhone,
                            false
                    )
            ),
            false
    );
    final var parentTwo = new DbFieldNested<>(
            PARENT_TWO,
            "participant.parent_two",
            ParentDto::new,
            ParticipantDto::setParentTwo,
            List.of(
                    new DbField<>(FIRSTNAME, "shared.first.name", ParentDto::setFirstName, false),
                    new DbField<>(LASTNAME, "shared.last.name", ParentDto::setLastName, false),
                    new DbField<>(EMAIL, "shared.e.mail", ParentDto::setEmail, false)
            ),
            List.of(
                    new DbFieldTyped<>(
                            PHONE,
                            "shared.telefon",
                            PhoneNumber::fromString,
                            ParentDto::setPhone,
                            false
                    )
            ),
            false
    );

    return List.of(address, parentOne, parentTwo);
  }

  @Nonnull
  @Override
  public List<DbFieldRelation<ParticipantDto, ?>> getRelationFields() {
    final var courseLevel = new DbFieldRelation<>(
            COURSE_LEVEL,
            "courseLevel.course_level",
            code -> courseLevelService.getCourseLevelByCode(code).orElse(null),
            ParticipantDto::addCourseLevel,
            false
    );
    final var gender = new DbFieldRelation<>(
            GENDER,
            "shared.gender",
            genderName -> genderService.getGenderByName(genderName).orElse(null),
            ParticipantDto::setGender,
            false
    );

    return New.arrayList(courseLevel, gender);
  }

  @Nonnull
  @Override
  public ImportResult performImport(@Nonnull final String fileToken,
                                    @Nonnull final Map<String, String> mapping) {
    final var fileServerGate = globalInterface.getService(TempFileServerGate.class);
    final var fileDto = fileServerGate.loadFile(fileToken);
    final var csvRows = fileServerGate.parseRows(fileDto);

    final var mapper = new ParticipantCsvMapper(globalInterface);
    final var result = mapper.map(
            csvRows,
            mapping,
            getDbFields(),
            getTypedDbFields(),
            getNestedDbFields(),
            getRelationFields()
    );

    final var saveables = result.saveables();
    final var participantService = globalInterface.getService(IParticipantService.class);

    final var importErrors = New.<CsvMappingRowError>arrayList(result.errors());

    for (int i = 0; i < saveables.size(); i++) {
      final var saveable = saveables.get(i);

      processNestedEntities(saveable);

      try {
        participantService.createParticipant(saveable);
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

      final var errorFile = fileServerGate.parseRows(errorCsvRows, "participant_import_errors.csv");
      final var token = fileServerGate.store(errorFile);
      return ImportResult.partialSuccess(token, errorFile.filename());
    }

    return ImportResult.completeSuccessInstance();
  }

  @Nonnull
  private String buildImportErrorMessage(@Nonnull final ParticipantDto participant,
                                         @Nonnull final DataIntegrityViolationException exception) {
    final var mostSpecificCause = exception.getMostSpecificCause();
    final var message = mostSpecificCause.getMessage();

    if (message != null &&
            message.contains(ERROR_DUPLICATE_KEY_VALUE_VIOLATES_UNIQUE_CONSTRAINT) &&
            message.toLowerCase().contains(EMAIL)) {
      return TranslationHelper.getTranslation(globalInterface, "common.participant.with.this.email.already.exists.0", participant.getEmail());
    }

    return TranslationHelper.getTranslation(globalInterface, "common.datenbankfehler.beim.import.0", message);
  }

  private void processNestedEntities(@Nonnull final ParticipantDto participant) {
    final var addressService = globalInterface.getService(AddressService.class);
    final var parentService = globalInterface.getService(ParentService.class);

    if (!participant.getAddress().isEmpty()) {
      final var addressDto = addressService.findOrCreateAddress(participant.getAddress());
      participant.setAddress(addressDto);
    }

    if (!participant.getParentOne().isEmpty()) {
      final var parentOneDto = parentService.findOrCreateParent(participant.getParentOne());
      participant.setParentOne(parentOneDto);
    }

    if (!participant.getParentTwo().isEmpty()) {
      final var parentTwoDto = parentService.findOrCreateParent(participant.getParentTwo());
      participant.setParentTwo(parentTwoDto);
    }
  }
}