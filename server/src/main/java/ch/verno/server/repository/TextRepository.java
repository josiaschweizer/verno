package ch.verno.server.repository;

import ch.verno.db.entity.text.TextEntity;
import ch.verno.db.jpa.text.SpringDataTextJpaRepository;
import org.springframework.stereotype.Repository;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

@Repository
public class TextRepository {

  @Nonnull private final SpringDataTextJpaRepository jpaRepository;

  public TextRepository(@Nonnull SpringDataTextJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Nonnull
  public List<TextEntity> findAll() {
    return jpaRepository.findAll();
  }

  @Nonnull
  public Optional<TextEntity> findById(@Nonnull final Long id) {
    return jpaRepository.findById(id);
  }

  @Nonnull
  public Optional<TextEntity> findByIdentifier(@Nonnull String identifier) {
    return jpaRepository.findByIdentifier(identifier);
  }

  @Nonnull
  public TextEntity save(@Nonnull final TextEntity text) {
    return jpaRepository.save(text);
  }

  public void delete(@Nonnull final TextEntity text) {
    jpaRepository.delete(text);
  }

}
