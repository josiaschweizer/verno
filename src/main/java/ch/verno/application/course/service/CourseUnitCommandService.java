package ch.verno.application.course.service;

import ch.verno.application.course.command.CreateCourseUnitCommand;
import ch.verno.domain.model.course.CourseUnit;
import ch.verno.domain.model.course.CourseUnitWeek;
import ch.verno.domain.repository.CourseUnitRepositoryPort;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class CourseUnitCommandService {

  @Nonnull
  private final CourseUnitRepositoryPort repo;

  public CourseUnitCommandService(@Nonnull final CourseUnitRepositoryPort repo) {
    this.repo = repo;
  }

  @Nonnull
  @Transactional
  public CourseUnit create(@Nonnull final CreateCourseUnitCommand cmd) {
    final var weeks = cmd.weeks().stream()
        .map(w -> new CourseUnitWeek(w.year(), w.calendarWeek()))
        .toList();

    final var unit = new CourseUnit(
        0L,
        cmd.name(),
        weeks,
        Instant.now()
    );

    return repo.save(unit);
  }
}