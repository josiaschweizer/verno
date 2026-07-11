package ch.verno.db.jpa.file;

import ch.verno.db.entity.file.StoredFileEntity;
import ch.verno.db.jpa.base.AbstractEntityJpaRepository;

public interface SpringDataStoredFileJpaRepository extends AbstractEntityJpaRepository<StoredFileEntity, Long> {
}
