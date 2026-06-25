package ch.verno.server.service.base;

import ch.verno.contract.dto.filter.BaseFilter;
import ch.verno.contract.dto.table.base.BaseDto;
import ch.verno.contract.dto.table.base.SortOrderDto;
import ch.verno.server.mapper.db.base.IEntityMapper;
import ch.verno.server.repository.base.IEntityRepository;
import ch.verno.server.spec.BaseSpec;
import ch.verno.server.spec.PageHelper;
import jakarta.annotation.Nonnull;

import java.util.List;

public class AbstractSpecEntityService<
        ENTITY,
        DTO extends BaseDto,
        REPOSITORY extends IEntityRepository<ENTITY, Long>,
        MAPPER extends IEntityMapper<ENTITY, DTO>,
        SPEC extends BaseSpec<ENTITY, FILTER>,
        FILTER extends BaseFilter
        > extends AbstractEntityService<ENTITY, DTO, REPOSITORY, MAPPER> {

  @Nonnull private final SPEC spec;

  protected AbstractSpecEntityService(@Nonnull final REPOSITORY repository,
                                      @Nonnull final MAPPER mapper,
                                      @Nonnull final SPEC spec) {
    super(repository, mapper);
    this.spec = spec;
  }

  @Nonnull
  public List<DTO> findAll(@Nonnull final FILTER filter,
                           final int offset,
                           final int limit,
                           @Nonnull final List<SortOrderDto> sortOrders) {
    final var specification = spec.getSpecification(filter);
    final var page = PageHelper.createPageRequest(offset, limit, sortOrders);

    return getRepository().findAll(specification, page)
            .stream()
            .map(getMapper()::toSimpleDto)
            .toList();
  }

}
