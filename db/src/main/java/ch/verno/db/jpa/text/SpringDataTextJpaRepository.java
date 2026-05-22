package ch.verno.db.jpa.text;

import ch.verno.db.entity.text.TextEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.annotation.Nonnull;
import java.util.Optional;

public interface SpringDataTextJpaRepository extends JpaRepository<TextEntity, Long> {

  @Nonnull
  Optional<TextEntity> findByIdentifier(@Nonnull String identifier);

}
