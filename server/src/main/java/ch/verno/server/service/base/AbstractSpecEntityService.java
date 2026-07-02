package ch.verno.server.service.base;

import ch.verno.contract.dto.filter.BaseFilter;
import ch.verno.contract.dto.table.base.BaseDto;
import ch.verno.contract.dto.table.base.SortOrderDto;
import ch.verno.server.mapper.base.IEntityMapper;
import ch.verno.server.repository.base.IEntityRepository;
import ch.verno.server.spec.BaseSpec;
import ch.verno.server.spec.PageHelper;
import jakarta.annotation.Nonnull;

import java.util.List;
import java.util.function.Supplier;

public class AbstractSpecEntityService<
        ID,
        ENTITY,
        DTO extends BaseDto<ID>,
        REPOSITORY extends IEntityRepository<ENTITY, ID>,
        MAPPER extends IEntityMapper<ENTITY, DTO>,
        SPEC extends BaseSpec<ENTITY, FILTER>,
        FILTER extends BaseFilter
        > extends AbstractEntityService<ID, ENTITY, DTO, REPOSITORY, MAPPER> {

  @Nonnull private final Supplier<SPEC> spec;

  protected AbstractSpecEntityService(@Nonnull final REPOSITORY repository,
                                      @Nonnull final MAPPER mapper,
                                      @Nonnull final Supplier<SPEC> spec) {
    super(repository, mapper);
    this.spec = spec;
  }

  @Nonnull
  public List<DTO> findAll(@Nonnull final FILTER filter,
                           @Nonnull final List<SortOrderDto> sortOrders,
                           final int offset,
                           final int limit) {
    final var specInstance = spec.get();
    final var specification = specInstance.getSpecification(filter);
    final var resolvedSortOrders = specInstance.resolveSortOrders(sortOrders);

    final var page = PageHelper.createPageRequest(resolvedSortOrders, offset, limit);

    return getRepository().findAll(specification, page)
            .stream()
            .map(getMapper()::toSimpleDto)
            .toList();
  }

}
