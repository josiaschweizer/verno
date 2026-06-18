package ch.verno.server.repository.text;

import ch.verno.db.entity.text.TextEntity;
import ch.verno.db.jpa.text.SpringDataTextJpaRepository;
import ch.verno.lib.language.Language;
import ch.verno.server.repository.base.AbstractEntityRepository;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TextRepository extends AbstractEntityRepository<
        TextEntity,
        Long,
        SpringDataTextJpaRepository> {

  public TextRepository(@Nonnull final SpringDataTextJpaRepository repository) {
    super(repository);
  }

  @Nonnull
  public List<TextEntity> findByIdentifier(@Nonnull final String identifier) {
    return getRepository().findByIdentifier(identifier);
  }

  @Nonnull
  public List<TextEntity> findByIdentifierAndSubIdentifier(@Nonnull final String identifier,
                                                           @Nonnull final String subIdentifier) {
    return getRepository().findByIdentifierAndSubIdentifier(identifier, subIdentifier);
  }

  @Nonnull
  public Optional<TextEntity> findByIdentifierAndSubIdentifierAndLanguage(@Nonnull final String identifier,
                                                                          @Nonnull final String subIdentifier,
                                                                          @Nonnull final Language language) {
    return getRepository().findByIdentifierAndSubIdentifierAndLanguageCode(
            identifier,
            subIdentifier,
            language.getCode()
    );
  }
}