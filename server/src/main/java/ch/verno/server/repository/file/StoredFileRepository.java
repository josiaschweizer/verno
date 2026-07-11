package ch.verno.server.repository.file;

import ch.verno.db.entity.file.StoredFileEntity;
import ch.verno.db.jpa.file.SpringDataStoredFileJpaRepository;
import ch.verno.server.repository.base.AbstractEntityRepository;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Repository;

@Repository
public class StoredFileRepository extends AbstractEntityRepository<
        StoredFileEntity,
        Long,
        SpringDataStoredFileJpaRepository> {


  public StoredFileRepository(@Nonnull final SpringDataStoredFileJpaRepository repository) {
    super(repository);
  }
}
