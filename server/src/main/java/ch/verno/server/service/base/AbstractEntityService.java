package ch.verno.server.service.base;

import ch.verno.contract.dto.response.base.delete.DeleteResponse;
import ch.verno.contract.dto.table.base.BaseDto;
import ch.verno.lib.exception.ExceptionUtil;
import ch.verno.lib.lang.ObjectUtil;
import ch.verno.server.mapper.base.AbstractEntityMapper;
import ch.verno.server.repository.base.IEntityRepository;
import jakarta.annotation.Nonnull;
import org.hibernate.service.spi.ServiceException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public abstract class AbstractEntityService<
        ID,
        ENTITY,
        DTO extends BaseDto<ID>,
        REPOSITORY extends IEntityRepository<ENTITY, ID>,
        MAPPER extends AbstractEntityMapper<ENTITY, DTO>
        > implements IEntityServiceExtendedById<DTO, ID> {

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

  @Override
  @Transactional(readOnly = true)
  public boolean existsById(@Nonnull final ID id) {
    return repository.existsById(id);
  }

  @Nonnull
  @Override
  @Transactional(readOnly = true)
  public Optional<DTO> findById(@Nonnull final ID id) {
    return repository.findById(id)
            .map(mapper::toDto);
  }

  @Nonnull
  @Override
  @Transactional(readOnly = true)
  public DTO findByIdRequired(@Nonnull final ID id) {
    return getMapper().toDto(repository.findById(id)
            .orElseThrow(ExceptionUtil::toEntityNotFoundException));
  }

  @Nonnull
  @Override
  @Transactional(readOnly = true)
  public List<DTO> findAll() {
    return repository.findAll()
            .stream()
            .map(mapper::toDto)
            .toList();
  }

  @Nonnull
  @Override
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
  protected DTO update(@Nonnull DTO dto) {
    if (dto.getId() == null) {
      throw new ServiceException("Cannot update an entity without an id");
    }

    ENTITY entity = repository.findById(dto.getId()).orElseThrow();

    mapper.updateEntity(entity, dto);
    entity = repository.save(entity);
    return mapper.toDto(entity);
  }

  @Nonnull
  @Override
  @Transactional
  public DeleteResponse deleteById(@Nonnull final ID id) {
    if (ObjectUtil.isEmpty(id)) {
      return DeleteResponse.emptyId();
    }

    final var result = repository.deleteById(id);
    return result ? DeleteResponse.success() : DeleteResponse.faulty();
  }

  @Nonnull
  @Override
  @Transactional
  public DeleteResponse delete(@Nonnull final DTO dto) {
    if (dto.getId() == null || ObjectUtil.isEmpty(dto.getId())) {
      return DeleteResponse.emptyId();
    }

    final var result = repository.deleteById(dto.getId());
    return result ? DeleteResponse.success() : DeleteResponse.faulty();
  }

  @Override
  @Transactional(readOnly = true)
  public long count() {
    return repository.count();
  }

  @Override
  @Transactional(readOnly = true)
  public void flush() {
    repository.flush();
  }

}