package ch.verno.db.jpa.text;

import ch.verno.db.entity.text.TextEntity;
import ch.verno.db.jpa.base.AbstractEntityJpaRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public interface SpringDataTextJpaRepository extends AbstractEntityJpaRepository<TextEntity, Long> {

  @Nonnull
  List<TextEntity> findByIdentifier(@Nonnull String identifier);

  @Nonnull
  List<TextEntity> findByIdentifierAndSubIdentifier(
          @Nonnull String identifier,
          @Nullable String subIdentifier
  );

  @Nonnull
  Optional<TextEntity> findByIdentifierAndSubIdentifierAndLanguageCode(
          @Nonnull String identifier,
          @Nullable String subIdentifier,
          @Nonnull String languageCode
  );

}
