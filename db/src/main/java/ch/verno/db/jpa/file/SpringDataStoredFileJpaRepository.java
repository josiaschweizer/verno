package ch.verno.db.jpa.file;

import ch.verno.db.entity.file.StoredFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataStoredFileJpaRepository extends JpaRepository<StoredFileEntity, Long> {
}
