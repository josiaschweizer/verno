package ch.verno.server.service.intern.table.address;

import ch.verno.contract.dto.table.address.AddressDto;
import ch.verno.db.entity.address.AddressEntity;
import ch.verno.lib.Lazy;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.bo.BoFactory;
import ch.verno.server.bo.table.address.AddressBo;
import ch.verno.server.mapper.db.address.AddressMapper;
import ch.verno.server.repository.address.AddressRepository;
import ch.verno.server.service.base.AbstractEntityService;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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
  @Transactional(readOnly = true)
  public Optional<AddressEntity> findEntityById(@Nonnull final Long id) {
    return getRepository().findById(id);
  }

  @Nonnull
  @Transactional(readOnly = true)
  public Optional<AddressDto> findByFields(@Nonnull final String street,
                                           @Nonnull final String houseNumber,
                                           @Nonnull final String zipCode,
                                           @Nonnull final String city,
                                           @Nonnull final String country) {
    return getRepository()
            .findByFields(street, houseNumber, zipCode, city, country)
            .map(getMapper()::toSimpleDto);
  }

  @Nonnull
  @Transactional
  public AddressEntity saveEntity(@Nonnull final AddressEntity entity) {
    return getRepository().save(entity);
  }

}