package ch.verno.server.service.intern;

import ch.verno.common.db.dto.table.ParentDto;
import ch.verno.common.db.service.IParentService;
import ch.verno.common.exceptions.db.DBNotFoundException;
import ch.verno.common.exceptions.db.DBNotFoundReason;
import ch.verno.db.entity.ParentEntity;
import ch.verno.db.entity.tenant.TenantEntity;
import ch.verno.common.tenant.TenantContext;
import ch.verno.server.mapper.ParentMapper;
import ch.verno.server.repository.ParentRepository;
import ch.verno.server.service.helper.ServiceHelper;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ParentService implements IParentService {

  @Nonnull
  private final ParentRepository parentRepository;

  public ParentService(@Nonnull final ParentRepository parentRepository) {
    this.parentRepository = parentRepository;
  }

  @Nonnull
  @Override
  public ParentDto createParent(@Nonnull final ParentDto parentDto) {
    final long tenantId = TenantContext.getRequired();

    final var entity = new ParentEntity(
            TenantEntity.ref(tenantId),
            ServiceHelper.safeString(parentDto.getFirstName()),
            ServiceHelper.safeString(parentDto.getLastName()),
            ServiceHelper.safeString(parentDto.getEmail()),
            ServiceHelper.safeString(parentDto.getPhone().toString())
    );

    return ParentMapper.toDto(parentRepository.save(entity));
  }

  @Nonnull
  @Override
  public ParentDto updateParent(@Nonnull final ParentDto parentDto) {
    throw new UnsupportedOperationException("Update parent not yet implemented");
  }

  @Nonnull
  @Override
  @Transactional(readOnly = true)
  public ParentDto getParentById(@Nonnull final Long id) {
    final long tenantId = TenantContext.getRequired();

    final var found = parentRepository.findById(id, tenantId);
    if (found.isEmpty()) {
      throw new DBNotFoundException(DBNotFoundReason.PARENT_BY_ID_NOT_FOUND, id);
    }

    return ParentMapper.toDto(found.get());
  }

  @Nonnull
  @Override
  @Transactional(readOnly = true)
  public List<ParentDto> getParents() {
    final long tenantId = TenantContext.getRequired();

    return parentRepository.findAll(tenantId)
            .stream()
            .map(ParentMapper::toDto)
            .toList();
  }

  @Nonnull
  public ParentDto findOrCreateParent(@Nonnull final ParentDto parentDto) {
    final long tenantId = TenantContext.getRequired();

    final var firstname = ServiceHelper.safeString(parentDto.getFirstName());
    final var lastname = ServiceHelper.safeString(parentDto.getLastName());
    final var email = ServiceHelper.safeString(parentDto.getEmail());
    final var phone = ServiceHelper.safeString(parentDto.getPhone().toString());

    final var existing = parentRepository.findByFields(tenantId, firstname, lastname, email, phone);
    if (existing.isPresent()) {
      return ParentMapper.toDto(existing.get());
    }

    final var entity = new ParentEntity(TenantEntity.ref(tenantId), firstname, lastname, email, phone);
    return ParentMapper.toDto(parentRepository.save(entity));
  }
}