package ch.verno.server.persistence.adapter;

import ch.verno.domain.model.course.CourseUnit;
import ch.verno.domain.repository.CourseUnitRepositoryPort;
import ch.verno.server.persistence.mapper.CourseUnitMapper;
import ch.verno.server.persistence.repository.CourseUnitRepository;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class CourseUnitJpaAdapter implements CourseUnitRepositoryPort {

  @Nonnull
  private final CourseUnitRepository repo;

  public CourseUnitJpaAdapter(@Nonnull final CourseUnitRepository repo) {
    this.repo = repo;
  }

  @Nonnull
  @Override
  public Optional<CourseUnit> findById(final long id) {
    return repo.findById(id).map(CourseUnitMapper::toDomain);
  }

  @Nonnull
  @Override
  public CourseUnit save(@Nonnull final CourseUnit unit) {
    if (unit.id() == 0L) {
      final var saved = repo.save(CourseUnitMapper.toNewEntity(unit));
      return CourseUnitMapper.toDomain(saved);
    }

    final var existing = repo.findById(unit.id())
        .orElseThrow(() -> new IllegalArgumentException("CourseUnit not found: " + unit.id()));

    // name updaten + weeks ersetzen
    // (Name ist private -> in Entity haben wir keinen Setter. Entweder Setter hinzufügen oder neu erstellen.)
    // Minimal: neu erstellen via merge-Strategie: Setter hinzufügen.
    throw new UnsupportedOperationException("Add setters or implement merge strategy for updates.");
  }

  @Override
  public void deleteById(final long id) {
    repo.deleteById(id);
  }
}