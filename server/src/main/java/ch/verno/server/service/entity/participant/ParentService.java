package ch.verno.server.service.entity.participant;

import ch.verno.common.dto.ui.phonenumber.PhoneNumber;
import ch.verno.contract.dto.table.participant.ParentDto;
import ch.verno.db.entity.participant.ParentEntity;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.mapper.participant.ParentMapper;
import ch.verno.server.repository.participant.ParentRepository;
import ch.verno.server.service.base.AbstractEntityServiceLongId;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class ParentService extends AbstractEntityServiceLongId<
        ParentEntity,
        ParentDto,
        ParentRepository,
        ParentMapper> {

  public ParentService(@Nonnull final ServerBean serverBean) {
    super(serverBean.get(ParentRepository.class), serverBean.get(ParentMapper.class));
  }

  @Nonnull
  public Optional<ParentDto> findByFields(@Nonnull final String firstname,
                                          @Nonnull final String lastname,
                                          @Nonnull final String email,
                                          @Nonnull final PhoneNumber phone) {
    return getRepository().findByFields(firstname, lastname, email, phone.toString()).map(getMapper()::toDto);
  }
}
