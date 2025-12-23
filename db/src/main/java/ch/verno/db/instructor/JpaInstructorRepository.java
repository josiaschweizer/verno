package ch.verno.db.instructor;

import ch.verno.server.repository.InstructorRepository;
import ch.verno.server.entity.InstructorEntity;
import jakarta.annotation.Nonnull;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.Optional;

public class JpaInstructorRepository implements InstructorRepository {

  @Nonnull
  private final SpringDataInstructorJpaRepository springDataInstructorJpaRepository;

  public JpaInstructorRepository(@Nonnull final SpringDataInstructorJpaRepository springDataInstructorJpaRepository) {
    this.springDataInstructorJpaRepository = springDataInstructorJpaRepository;
  }

  @Override
  public @NonNull Optional<InstructorEntity> findById(@NonNull final Long id) {
    return springDataInstructorJpaRepository.findById(id);
  }

  @Override
  public @NonNull List<InstructorEntity> findAll() {
    return springDataInstructorJpaRepository.findAll();
  }

  @Override
  public void save(@NonNull final InstructorEntity entity) {
    springDataInstructorJpaRepository.save(entity);
  }
}
