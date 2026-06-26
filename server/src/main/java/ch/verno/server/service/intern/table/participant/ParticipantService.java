package ch.verno.server.service.intern.table.participant;

import ch.verno.contract.dto.filter.ParticipantFilter;
import ch.verno.contract.dto.table.participant.ParticipantDto;
import ch.verno.db.entity.participant.ParticipantEntity;
import ch.verno.lib.Lazy;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.mapper.db.participant.ParticipantMapper;
import ch.verno.server.repository.participant.ParticipantRepository;
import ch.verno.server.service.base.AbstractSpecEntityService;
import ch.verno.server.spec.ParticipantSpec;
import jakarta.annotation.Nonnull;

public class ParticipantService extends AbstractSpecEntityService<
        ParticipantEntity,
        ParticipantDto,
        ParticipantRepository,
        ParticipantMapper,
        ParticipantSpec,
        ParticipantFilter> {

  public ParticipantService(@Nonnull final ServerBean serverBean) {
    super(serverBean.get(ParticipantRepository.class), serverBean.get(ParticipantMapper.class), Lazy.of(ParticipantSpec::new));
  }
}
