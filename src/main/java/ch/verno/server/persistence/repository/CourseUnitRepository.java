package ch.verno.server.persistence.repository;

import ch.verno.server.persistence.entity.CourseUnitEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseUnitRepository extends JpaRepository<CourseUnitEntity, Long> {
}