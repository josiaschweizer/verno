package ch.verno.db.jpa;

import ch.verno.db.entity.InstructorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataInstructorJpaRepository extends JpaRepository<InstructorEntity, Long> {
}
