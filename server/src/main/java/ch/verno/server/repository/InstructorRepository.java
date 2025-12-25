package ch.verno.server.repository;

import ch.verno.db.entity.InstructorEntity;
import ch.verno.db.jpa.SpringDataInstructorJpaRepository;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class InstructorRepository {

  @Nonnull
  private final SpringDataInstructorJpaRepository springDataInstructorJpaRepository;

  public InstructorRepository(@Nonnull final SpringDataInstructorJpaRepository springDataInstructorJpaRepository) {
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

  @Nonnull
  public InstructorEntity save(@Nonnull final InstructorEntity entity) {
    return springDataInstructorJpaRepository.save(entity);
  }
}
