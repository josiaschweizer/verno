package ch.verno.ui.verno.dashboard.io.widgets.participant;

import ch.verno.common.db.dto.table.ParticipantDto;
import ch.verno.common.db.service.IParticipantService;
import ch.verno.common.api.dto.internal.file.temp.CsvMapDto;
import ch.verno.common.gate.GlobalInterface;
import ch.verno.common.lib.i18n.AbstractTranslationHelper;
import ch.verno.publ.Publ;
import ch.verno.ui.verno.dashboard.io.widgets.ExportEntityConfig;
import jakarta.annotation.Nonnull;

import java.util.LinkedHashMap;
import java.util.List;

public class ParticipantExportConfig extends AbstractTranslationHelper implements ExportEntityConfig<ParticipantDto> {

  @Nonnull private final GlobalInterface globalInterface;

  public ParticipantExportConfig(@Nonnull final GlobalInterface globalInterface) {
    this.globalInterface = globalInterface;
  }

  @Nonnull
  @Override
  public List<CsvMapDto> getRows() {
    final var participantService = globalInterface.getService(IParticipantService.class);
    final var participants = participantService.getAllParticipants();

    return participants.stream()
            .map(this::participantsToCsvMap)
            .toList();
  }

  @Nonnull
  private CsvMapDto participantsToCsvMap(@Nonnull final ParticipantDto participant) {
    final var row = new LinkedHashMap<String, String>();

    row.put(getTranslation(globalInterface, "shared.id"), participant.getId() != null ? participant.getId().toString() : Publ.EMPTY_STRING);
    row.put(getTranslation(globalInterface, "shared.first.name"), participant.getFirstName());
    row.put(getTranslation(globalInterface, "shared.last.name"), participant.getLastName());
    row.put(getTranslation(globalInterface, "shared.e.mail"), participant.getEmail());
    row.put(getTranslation(globalInterface, "shared.telefon"), participant.getPhoneString());
    row.put(getTranslation(globalInterface, "shared.gender"), participant.getGenderAsString());

    row.put(getTranslation(globalInterface, "shared.birthdate"), participant.getBirthdate() != null ? participant.getBirthdate().toString() : Publ.EMPTY_STRING);
    row.put(getTranslation(globalInterface, "shared.age"), participant.getAgeFromBirthday() != null ? participant.getAgeFromBirthday().toString() : Publ.EMPTY_STRING);

    row.put(getTranslation(globalInterface, "shared.active"), participant.isActive() ? getTranslation(globalInterface, "shared.yes") : getTranslation(globalInterface, "shared.no"));
    row.put(getTranslation(globalInterface, "shared.note"), participant.getNote());

    row.put(getTranslation(globalInterface, "courseLevel.course_levels"), participant.getCourseLevelsAsString());

    row.put(getTranslation(globalInterface, "shared.street"), participant.getAddress().getStreet());
    row.put(getTranslation(globalInterface, "shared.house.number"), participant.getAddress().getHouseNumber());
    row.put(getTranslation(globalInterface, "shared.zip.code"), participant.getAddress().getZipCode());
    row.put(getTranslation(globalInterface, "course.location"), participant.getAddress().getCity());
    row.put(getTranslation(globalInterface, "shared.country"), participant.getAddress().getCountry());

    row.put(getTranslation(globalInterface, "participant.parent.1.first.name"), participant.getParentOne().getFirstName());
    row.put(getTranslation(globalInterface, "participant.parent.1.last.name"), participant.getParentOne().getLastName());
    row.put(getTranslation(globalInterface, "participant.parent.1.email"), participant.getParentOne().getEmail());
    row.put(getTranslation(globalInterface, "participant.parent.1.phone"), participant.getParentOne().getPhone().toString());

    row.put(getTranslation(globalInterface, "participant.parent.2.first.name"), participant.getParentTwo().getFirstName());
    row.put(getTranslation(globalInterface, "participant.parent.2.last.name"), participant.getParentTwo().getLastName());
    row.put(getTranslation(globalInterface, "participant.parent.2.email"), participant.getParentTwo().getEmail());
    row.put(getTranslation(globalInterface, "participant.parent.2.phone"), participant.getParentTwo().getPhone().toString());

    return new CsvMapDto(row);
  }

  @Nonnull
  @Override
  public String getFileName() {
    return "participants.csv";
  }
}
