package ch.verno.server.repository.file;

import ch.verno.db.entity.file.StoredFileEntity;
import ch.verno.db.jpa.file.SpringDataStoredFileJpaRepository;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class StoredFileRepository {

  @Nonnull private final SpringDataStoredFileJpaRepository jpaRepository;

  public StoredFileRepository(@Nonnull final SpringDataStoredFileJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Nonnull
  public Optional<StoredFileEntity> findById(@Nonnull final Long id) {
    return jpaRepository.findById(id);
  }

  @Nonnull
  public List<StoredFileEntity> findAll() {
    return jpaRepository.findAll();
  }

  public void delete(@Nonnull final Long fileId) {
    jpaRepository.deleteById(fileId);
  }

  public void delete(@Nonnull final StoredFileEntity file) {
    jpaRepository.delete(file);
  }

  @Nonnull
  public StoredFileEntity save(@Nonnull final StoredFileEntity file) {
    return jpaRepository.save(file);
  }
}
