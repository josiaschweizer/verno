package ch.verno.db.jpa.instructor;

import ch.verno.db.entity.instructor.InstructorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SpringDataInstructorJpaRepository extends
        JpaRepository<InstructorEntity, Long>,
        JpaSpecificationExecutor<InstructorEntity> {
}
