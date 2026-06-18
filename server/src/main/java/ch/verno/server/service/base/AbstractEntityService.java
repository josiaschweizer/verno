package ch.verno.server.service.base;

import ch.verno.contract.dto.table.base.BaseDto;
import ch.verno.server.mapper.base.IEntityMapper;
import ch.verno.server.repository.base.IEntityRepository;
import jakarta.annotation.Nonnull;
import org.hibernate.service.spi.ServiceException;

import java.util.List;
import java.util.Optional;

public abstract class AbstractEntityService<
        ENTITY,
        DTO extends BaseDto,
        REPOSITORY extends IEntityRepository<ENTITY, Long>,
        MAPPER extends IEntityMapper<ENTITY, DTO>
        > implements IEntityService<DTO> {

  @Nonnull private final REPOSITORY repository;
  @Nonnull private final MAPPER mapper;

  protected AbstractEntityService(@Nonnull REPOSITORY repository,
                                  @Nonnull MAPPER mapper) {
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
  @Override
  public Optional<DTO> findById(@Nonnull Long id) {
    return repository.findById(id)
            .map(mapper::toSimpleDto);
  }

  @Nonnull
  @Override
  public List<DTO> findAll() {
    return repository.findAll()
            .stream()
            .map(mapper::toSimpleDto)
            .toList();
  }

  @Nonnull
  @Override
  public DTO save(@Nonnull DTO dto) {
    if (dto.getId() == null) {
      return create(dto);
    }

    return update(dto);
  }

  @Nonnull
  protected DTO create(@Nonnull DTO dto) {
    ENTITY entity = mapper.toNewEntity(dto);
    entity = repository.save(entity);
    return mapper.toSimpleDto(entity);
  }

  @Nonnull
  protected DTO update(@Nonnull DTO dto) {
    if (dto.getId() == null) {
      throw new ServiceException("Cannot update an entity without an id");
    }

    ENTITY entity = repository.findById(dto.getId())
            .orElseThrow();

    mapper.updateEntity(entity, dto);
    entity = repository.save(entity);
    return mapper.toSimpleDto(entity);
  }

  @Override
  public void deleteById(@Nonnull Long id) {
    repository.deleteById(id);
  }

  @Override
  public void delete(@Nonnull final DTO dto) {
    if (dto.getId() == null) {
      throw new ServiceException("Cannot delete entity with null id");
    }

    repository.deleteById(dto.getId());
  }

  @Override
  public long count() {
    return repository.count();
  }

}