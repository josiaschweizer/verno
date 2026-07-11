package ch.verno.server.mapper.file;

import ch.verno.contract.dto.table.file.StoredFileDto;
import ch.verno.db.entity.file.StoredFileEntity;
import ch.verno.server.mapper.base.AbstractEntityMapper;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class StoredFileMapper extends AbstractEntityMapper<StoredFileEntity, StoredFileDto> {

  @Nonnull
  @Override
  public StoredFileDto toDto(@Nonnull final StoredFileEntity entity) {
    final var dto = StoredFileDto.empty();

    dto.setId(entity.getId());
    dto.setTenantId(entity.getTenant() != null ? entity.getTenant().getId() : null);
    dto.setFilename(entity.getFilename());
    dto.setContentType(entity.getContentType());
    dto.setSize(entity.getSize());
    dto.setChecksumSha256(entity.getChecksumSha256());

    return dto;
  }

  @Nonnull
  @Override
  public StoredFileEntity toNewEntity(@Nonnull final StoredFileDto dto) {
    final var entity = new StoredFileEntity(
            null,
            dto.getFilename(),
            dto.getContentType(),
            dto.getSize(),
            dto.getChecksumSha256(),
            null,
            Instant.now()
    );

    updateEntity(entity, dto);
    return entity;
  }

  @Override
  public void updateEntity(@Nonnull final StoredFileEntity entity,
                           @Nonnull final StoredFileDto dto) {
    entity.setFilename(dto.getFilename());
    entity.setContentType(dto.getContentType());
    entity.setSize(dto.getSize());
    entity.setChecksumSha256(dto.getChecksumSha256());
  }
}