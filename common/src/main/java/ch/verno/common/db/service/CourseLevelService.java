package ch.verno.common.db.service;

import ch.verno.server.repository.CourseLevelRepository;
import ch.verno.server.entity.CourseLevelEntity;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CourseLevelService {

  @Nonnull
  private final CourseLevelRepository courseLevelRepository;

  public CourseLevelService(@Nonnull final CourseLevelRepository courseLevelRepository) {
    this.courseLevelRepository = courseLevelRepository;
  }

  @Nonnull
  @Transactional(readOnly = true)
  public CourseLevelEntity getCourseLevelById(@Nonnull final Long id) {
    return courseLevelRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Participant not found with id: " + id));
  }

  @Nonnull
  @Transactional(readOnly = true)
  public List<CourseLevelEntity> getAllCourseLevels() {
    return courseLevelRepository.findAll();
  }
}
