package ch.verno.server.service.intern.table.file;

import ch.verno.contract.dto.table.file.StoredFileDto;
import ch.verno.db.entity.file.StoredFileEntity;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.mapper.file.StoredFileMapper;
import ch.verno.server.repository.file.StoredFileRepository;
import ch.verno.server.service.base.AbstractEntityService;
import jakarta.annotation.Nonnull;

public class StoredFileService extends AbstractEntityService<
        Long,
        StoredFileEntity,
        StoredFileDto,
        StoredFileRepository,
        StoredFileMapper> {

  public StoredFileService(@Nonnull final ServerBean serverBean) {
    super(serverBean.get(StoredFileRepository.class), serverBean.get(StoredFileMapper.class));
  }
}
