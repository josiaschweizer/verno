package ch.verno.server.repository;

import ch.verno.db.entity.InstructorEntity;
import ch.verno.db.jpa.SpringDataInstructorJpaRepository;
import jakarta.annotation.Nonnull;

import java.util.List;
import java.util.Optional;

public class JpaInstructorRepository {

  @Nonnull
  private final SpringDataInstructorJpaRepository springDataInstructorJpaRepository;

  public JpaInstructorRepository(@Nonnull final SpringDataInstructorJpaRepository springDataInstructorJpaRepository) {
    this.springDataInstructorJpaRepository = springDataInstructorJpaRepository;
  }

  @Nonnull
  public Optional<InstructorEntity> findById(@Nonnull final Long id) {
    return springDataInstructorJpaRepository.findById(id);
  }

  @Nonnull
  public List<InstructorEntity> findAll() {
    return springDataInstructorJpaRepository.findAll();
  }

  public void save(@Nonnull final InstructorEntity entity) {
    springDataInstructorJpaRepository.save(entity);
  }
}
