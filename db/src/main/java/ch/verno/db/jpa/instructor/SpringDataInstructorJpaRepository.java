package ch.verno.db.jpa.instructor;

import ch.verno.db.entity.instructor.InstructorEntity;
import ch.verno.db.jpa.base.AbstractEntityJpaRepository;

public interface SpringDataInstructorJpaRepository extends AbstractEntityJpaRepository<InstructorEntity, Long> {
}
