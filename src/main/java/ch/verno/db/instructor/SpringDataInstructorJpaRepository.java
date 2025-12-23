package ch.verno.db.instructor;

import ch.verno.server.entity.InstructorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataInstructorJpaRepository extends JpaRepository<InstructorEntity, Long> {
}
