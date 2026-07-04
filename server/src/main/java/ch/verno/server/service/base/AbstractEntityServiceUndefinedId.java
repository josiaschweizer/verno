package ch.verno.server.service.base;

import ch.verno.contract.dto.response.base.delete.DeleteResponse;
import ch.verno.contract.dto.table.base.BaseDto;
import ch.verno.lib.lang.ObjectUtil;
import ch.verno.server.mapper.base.AbstractEntityMapper;
import ch.verno.server.repository.base.IEntityRepository;
import jakarta.annotation.Nonnull;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public abstract class AbstractEntityServiceUndefinedId<
        ENTITY,
        DTO extends BaseDto<?>,
        REPOSITORY extends IEntityRepository<ENTITY, ?>,
        MAPPER extends AbstractEntityMapper<ENTITY, DTO>
        > implements IEntityService<DTO> {

  @Nonnull private final REPOSITORY repository;
  @Nonnull private final MAPPER mapper;

  public AbstractEntityServiceUndefinedId(@Nonnull final REPOSITORY repository,
                                          @Nonnull final MAPPER mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @Nonnull
  protected REPOSITORY getRepository() {
    return repository;
  }

  @Nonnull
  protected MAPPER getMapper() {
    return mapper;
  }

  @Nonnull
  @Transactional(readOnly = true)
  public List<DTO> findAll() {
    return repository.findAll()
            .stream()
            .map(mapper::toDto)
            .toList();
  }

  @Nonnull
  @Transactional
  public DTO save(@Nonnull final DTO dto) {
    if (dto.getId() == null) {
      return create(dto);
    }

    return update(dto);
  }

  @Nonnull
  @Transactional
  protected DTO create(@Nonnull final DTO dto) {
    ENTITY entity = mapper.toNewEntity(dto);
    entity = repository.save(entity);
    return mapper.toDto(entity);
  }

  @Nonnull
  @Transactional
  protected abstract DTO update(@Nonnull DTO dto);

  @Nonnull
  @Transactional
  public DeleteResponse delete(@Nonnull final DTO dto) {
    if (dto.getId() == null || ObjectUtil.isEmpty(dto.getId())) {
      return DeleteResponse.emptyId();
    }

    getRepository().delete(getMapper().toNewEntity(dto));
    return DeleteResponse.success();
  }

  @Transactional(readOnly = true)
  public long count() {
    return repository.count();
  }

  @Transactional(readOnly = true)
  public void flush() {
    repository.flush();
  }

}
