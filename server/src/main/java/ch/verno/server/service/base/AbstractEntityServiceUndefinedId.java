package ch.verno.server.service.base;

import ch.verno.contract.dto.table.base.BaseDto;
import ch.verno.server.mapper.base.IEntityMapper;
import ch.verno.server.repository.base.IEntityRepository;
import jakarta.annotation.Nonnull;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public abstract class AbstractEntityServiceUndefinedId<
        ENTITY,
        DTO extends BaseDto<?>,
        REPOSITORY extends IEntityRepository<ENTITY, ?>,
        MAPPER extends IEntityMapper<ENTITY, DTO>
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
            .map(mapper::toSimpleDto)
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
    return mapper.toSimpleDto(entity);
  }

  @Nonnull
  @Transactional
  protected abstract DTO update(@Nonnull DTO dto);

  @Transactional
  public void delete(@Nonnull final DTO dto) {
    getRepository().delete(getMapper().toNewEntity(dto));
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
