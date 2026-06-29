package ch.verno.server.repository.instructor;

import ch.verno.db.entity.instructor.InstructorEntity;
import ch.verno.db.jpa.instructor.SpringDataInstructorJpaRepository;
import ch.verno.server.repository.base.AbstractEntityRepository;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Repository;

@Repository
public class InstructorRepository extends AbstractEntityRepository<
        InstructorEntity,
        Long,
        SpringDataInstructorJpaRepository> {

  public InstructorRepository(@Nonnull final SpringDataInstructorJpaRepository repository) {
    super(repository);
  }
}
