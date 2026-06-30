package ch.verno.server.service.entity.file;

import ch.verno.contract.dto.table.file.StoredFileDto;
import ch.verno.db.entity.file.StoredFileEntity;
import ch.verno.lib.Publ;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.mapper.file.StoredFileMapper;
import ch.verno.server.repository.file.StoredFileRepository;
import ch.verno.server.service.base.AbstractEntityService;
import jakarta.annotation.Nonnull;
import org.springframework.transaction.annotation.Transactional;

public class StoredFileService extends AbstractEntityService<
        Long,
        StoredFileEntity,
        StoredFileDto,
        StoredFileRepository,
        StoredFileMapper> {

  public StoredFileService(@Nonnull final ServerBean serverBean) {
    super(serverBean.get(StoredFileRepository.class), serverBean.get(StoredFileMapper.class));
  }

  @Nonnull
  @Transactional(readOnly = true)
  public String getStorageKey(@Nonnull final Long id) {
    final var byId = getRepository().findById(id);
    if (byId.isPresent()) {
      return byId.get().getStorageKey();
    } else {
      return Publ.EMPTY_STRING;
    }
  }

  @Transactional
  public void setStorageKey(@Nonnull final Long id,
                            @Nonnull final String storageKey) {
    final var entity = getRepository().findById(id)
            .orElseThrow(() -> new RuntimeException("File not found: " + id));
    entity.setStorageKey(storageKey);
    getRepository().save(entity);
  }
}
