package ch.verno.ui.verno.dashboard.io.widgets.participant;

import ch.verno.common.ui.base.components.entry.phonenumber.PhoneNumber;
import ch.verno.common.db.dto.table.AddressDto;
import ch.verno.common.db.dto.table.ParentDto;
import ch.verno.common.db.dto.table.ParticipantDto;
import ch.verno.common.db.service.IParticipantService;
import ch.verno.common.file.dto.CsvMapDto;
import ch.verno.common.file.FileServerGate;
import ch.verno.common.gate.GlobalInterface;
import ch.verno.publ.Publ;
import ch.verno.server.io.importing.dto.DbField;
import ch.verno.server.io.importing.dto.DbFieldNested;
import ch.verno.server.io.importing.dto.DbFieldTyped;
import ch.verno.server.mapper.csv.ParticipantCsvMapper;
import ch.verno.server.service.intern.AddressService;
import ch.verno.server.service.intern.ParentService;
import ch.verno.ui.verno.dashboard.io.widgets.ImportEntityConfig;
import ch.verno.ui.verno.dashboard.io.widgets.ImportResult;
import jakarta.annotation.Nonnull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ParticipantImportConfig implements ImportEntityConfig<ParticipantDto> {

  @Nonnull private final GlobalInterface globalInterface;

  public ParticipantImportConfig(@Nonnull final GlobalInterface globalInterface) {
    this.globalInterface = globalInterface;
  }

  @Nonnull
  @Override
  public List<DbField<ParticipantDto>> getDbFields() {
    return List.of(
            new DbField<>("firstname", "shared.first.name", ParticipantDto::setFirstName, true),
            new DbField<>("lastname", "shared.last.name", ParticipantDto::setLastName, true),
            new DbField<>("email", "shared.e.mail", ParticipantDto::setEmail, true),
            new DbField<>("note", "shared.note", ParticipantDto::setNote, false)
    );
  }

  @Override
  public List<DbFieldTyped<ParticipantDto, ?>> getTypedDbFields() {
    return List.of(
            new DbFieldTyped<>(
                    "birthdate",
                    "shared.birthdate",
                    ParticipantImportParser::parseDate,
                    ParticipantDto::setBirthdate,
                    false
            ),
            new DbFieldTyped<>(
                    "phone",
                    "shared.telefon",
                    PhoneNumber::fromString,
                    ParticipantDto::setPhone,
                    false
            )
    );
  }

  @Override
  public List<DbFieldNested<ParticipantDto, ?>> getNestedDbFields() {
    return List.of(
            new DbFieldNested<>(
                    "address",
                    "shared.address",
                    AddressDto::new,
                    ParticipantDto::setAddress,
                    List.of(
                            new DbField<>("street", "shared.street", AddressDto::setStreet, false),
                            new DbField<>("houseNumber", "shared.house.number", AddressDto::setHouseNumber, false),
                            new DbField<>("zipCode", "shared.zip.code", AddressDto::setZipCode, false),
                            new DbField<>("city", "shared.city", AddressDto::setCity, false),
                            new DbField<>("country", "shared.country", AddressDto::setCountry, false)
                    ),
                    List.of(),
                    false
            ),
            new DbFieldNested<>(
                    "parentOne",
                    "participant.parent_one",
                    ParentDto::new,
                    ParticipantDto::setParentOne,
                    List.of(
                            new DbField<>("firstName", "shared.first.name", ParentDto::setFirstName, false),
                            new DbField<>("lastName", "shared.last.name", ParentDto::setLastName, false),
                            new DbField<>("email", "shared.e.mail", ParentDto::setEmail, false)
                    ),
                    List.of(
                            new DbFieldTyped<>(
                                    "phone",
                                    "shared.telefon",
                                    PhoneNumber::fromString,
                                    ParentDto::setPhone,
                                    false
                            )
                    ),
                    false
            ),
            new DbFieldNested<>(
                    "parentTwo",
                    "participant.parent_two",
                    ParentDto::new,
                    ParticipantDto::setParentTwo,
                    List.of(
                            new DbField<>("firstName", "shared.first.name", ParentDto::setFirstName, false),
                            new DbField<>("lastName", "shared.last.name", ParentDto::setLastName, false),
                            new DbField<>("email", "shared.e.mail", ParentDto::setEmail, false)
                    ),
                    List.of(
                            new DbFieldTyped<>(
                                    "phone",
                                    "shared.telefon",
                                    PhoneNumber::fromString,
                                    ParentDto::setPhone,
                                    false
                            )
                    ),
                    false
            )
    );
  }

  @Nonnull
  @Override
  public ImportResult performImport(@Nonnull final String fileToken,
                                             @Nonnull final Map<String, String> mapping) {
    final var fileServerGate = globalInterface.getService(FileServerGate.class);
    final var fileDto = fileServerGate.loadFile(fileToken);
    final var csvRows = fileServerGate.parseRows(fileDto);

    final var mapper = new ParticipantCsvMapper();
    final var result = mapper.map(
            csvRows,
            mapping,
            getDbFields(),
            getTypedDbFields(),
            getNestedDbFields()
    );

    final var saveables = result.saveables();
    final var participantService = globalInterface.getService(IParticipantService.class);

    for (final var saveable : saveables) {
      processNestedEntities(saveable);
      participantService.createParticipant(saveable);
    }

    if (!result.errors().isEmpty()) {
      final var errorCsvRows = new ArrayList<CsvMapDto>();
      for (final var error : result.errors()) {
        final var csvRow = csvRows.get(error.rowIndex() - 1);
        csvRow.row().put("import_error", String.join(Publ.COMMA, error.message()));
        errorCsvRows.add(csvRow);
      }

      final var errorFile = fileServerGate.parseRows(errorCsvRows, "participant_import_errors.csv");
      final var token = fileServerGate.store(errorFile);
      return ImportResult.partialSuccess(token, errorFile.filename());
    }

    return ImportResult.completeSuccessInstance();
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
