package ch.verno.server.service.base;

import ch.verno.contract.dto.table.base.BaseDto;
import ch.verno.server.mapper.base.AbstractEntityMapper;
import ch.verno.server.repository.base.IEntityRepository;
import jakarta.annotation.Nonnull;

public class AbstractEntityServiceLongId<
        ENTITY,
        DTO extends BaseDto<Long>,
        REPOSITORY extends IEntityRepository<ENTITY, Long>,
        MAPPER extends AbstractEntityMapper<ENTITY, DTO>
        > extends AbstractEntityService<Long, ENTITY, DTO, REPOSITORY, MAPPER> {

  protected AbstractEntityServiceLongId(@Nonnull final REPOSITORY repository,
                                        @Nonnull final MAPPER mapper) {
    super(repository, mapper);
  }
}
