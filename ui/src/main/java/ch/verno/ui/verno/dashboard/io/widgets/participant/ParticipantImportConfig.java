package ch.verno.ui.verno.dashboard.io.widgets.participant;

import ch.verno.common.base.components.entry.phonenumber.PhoneNumber;
import ch.verno.common.db.dto.ParticipantDto;
import ch.verno.common.db.service.IParticipantService;
import ch.verno.common.file.FileServerGate;
import ch.verno.common.gate.VernoApplicationGate;
import ch.verno.server.io.importing.dto.DbField;
import ch.verno.server.io.importing.dto.DbFieldTyped;
import ch.verno.server.mapper.csv.ParticipantCsvMapper;
import ch.verno.ui.base.components.notification.NotificationFactory;
import ch.verno.ui.verno.dashboard.io.widgets.ImportEntityConfig;
import jakarta.annotation.Nonnull;

import java.util.List;
import java.util.Map;

public class ParticipantImportConfig implements ImportEntityConfig<ParticipantDto> {

  @Nonnull private final VernoApplicationGate vernoApplicationGate;

  public ParticipantImportConfig(@Nonnull final VernoApplicationGate vernoApplicationGate) {
    this.vernoApplicationGate = vernoApplicationGate;
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
  public boolean performImport(@Nonnull final String fileToken,
                                @Nonnull final Map<String, String> mapping) {
    final var fileServerGate = vernoApplicationGate.getService(FileServerGate.class);
    final var fileDto = fileServerGate.loadFile(fileToken);
    final var csvRows = fileServerGate.parseRows(fileDto);

    final var mapper = new ParticipantCsvMapper();
    final var result = mapper.map(
            csvRows,
            mapping,
            getDbFields(),
            getTypedDbFields()
    );

    final var saveables = result.saveables();
    final var participantService = vernoApplicationGate.getService(IParticipantService.class);
    for (final var saveable : saveables) {
      participantService.createParticipant(saveable);
    }

    for (final var error : result.errors()) {
      NotificationFactory.showErrorNotification(error.message());
    }

    return true;
  }
}
