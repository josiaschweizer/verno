package ch.verno.db.jpa;

import ch.verno.db.entity.mandant.MandantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface SpringDataMandantJpaRepository extends JpaRepository<MandantEntity, Long> {

  Optional<MandantEntity> findBySlug(String slug);

  @Query("select m.id from MandantEntity m where m.slug = :slug")
  Optional<Long> findIdBySlug(String slug);

}
