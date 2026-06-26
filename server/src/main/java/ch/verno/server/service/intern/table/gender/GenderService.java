package ch.verno.server.service.intern.table.gender;

import ch.verno.contract.dto.table.gender.GenderDto;
import ch.verno.db.entity.gender.GenderEntity;
import ch.verno.lib.Lazy;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.bo.BoFactory;
import ch.verno.server.bo.table.gender.GenderBo;
import ch.verno.server.mapper.gender.GenderMapper;
import ch.verno.server.repository.gender.GenderRepository;
import ch.verno.server.service.base.AbstractEntityServiceLongId;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class GenderService extends AbstractEntityServiceLongId<
        GenderEntity,
        GenderDto,
        GenderRepository,
        GenderMapper> {

  @Nonnull private final Lazy<GenderBo> genderBo;

  public GenderService(@Nonnull final ServerBean serverBean) {
    super(serverBean.get(GenderRepository.class), serverBean.get(GenderMapper.class));
    this.genderBo = Lazy.of(() -> BoFactory.getInstance(serverBean).get(GenderBo.class));
  }

  @Nonnull
  @Transactional(readOnly = true)
  public Optional<GenderDto> findByName(@Nonnull final String name) {
    return genderBo.get().findByName(name);
  }

  @Nonnull
  @Override
  @Transactional(readOnly = true)
  public List<GenderDto> findAll() {
    return genderBo.get().findAll();
  }

  @Nonnull
  @Override
  public GenderDto save(@Nonnull final GenderDto dto) {
    return genderBo.get().save(dto);
  }
}