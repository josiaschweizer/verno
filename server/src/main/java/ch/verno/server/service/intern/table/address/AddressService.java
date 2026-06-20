package ch.verno.server.service.intern.table.address;

import ch.verno.contract.dto.table.address.AddressDto;
import ch.verno.db.entity.address.AddressEntity;
import ch.verno.lib.Lazy;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.bo.BoFactory;
import ch.verno.server.bo.table.address.AddressBo;
import ch.verno.server.mapper.address.AddressMapper;
import ch.verno.server.repository.address.AddressRepository;
import ch.verno.server.service.base.AbstractEntityService;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AddressService extends AbstractEntityService<
        AddressEntity,
        AddressDto,
        AddressRepository,
        AddressMapper> {

  @Nonnull private final Lazy<AddressBo> addressBo;

  public AddressService(@Nonnull final ServerBean bean) {
    super(bean.get(AddressRepository.class), bean.get(AddressMapper.class));
    this.addressBo = Lazy.of(() -> bean.get(BoFactory.class).get(AddressBo.class));
  }

  @Nonnull
  public AddressDto findOrCreateAddress(@Nonnull final AddressDto dto) {
    return getMapper().toSimpleDto(addressBo.get().findOrCreate(dto));
  }

  @Nullable
  public AddressDto saveOrUpdateAddress(@Nullable final AddressDto dto) {
    final var entity = addressBo.get().saveOrUpdate(dto);
    return entity == null ? null : getMapper().toSimpleDto(entity);
  }

}