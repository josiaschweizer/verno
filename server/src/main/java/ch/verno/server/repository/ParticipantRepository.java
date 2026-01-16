package ch.verno.server.repository;

import ch.verno.db.entity.CourseEntity;
import ch.verno.db.entity.ParticipantEntity;
import ch.verno.db.jpa.SpringDataParticipantJpaRepository;
import jakarta.annotation.Nonnull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ParticipantRepository {

  private final SpringDataParticipantJpaRepository jpaRepository;

  ParticipantRepository(@Nonnull final SpringDataParticipantJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Nonnull
  public Optional<ParticipantEntity> findById(@Nonnull final Long id) {
    return jpaRepository.findById(id);
  }

  @Nonnull
  public List<ParticipantEntity> findAll() {
    return jpaRepository.findAll();
  }

  @Nonnull
  public List<ParticipantEntity> findByCourse(@Nonnull final CourseEntity course) {
    return jpaRepository.findByCoursesAndActiveTrue(course);
  }

  public boolean existsByCourseId(@Nonnull final Long courseId) {
    return jpaRepository.existsByCourses_Id(courseId);
  }

  @Nonnull
  public Page<ParticipantEntity> findAll(@Nonnull final Pageable pageable) {
    return jpaRepository.findAll(pageable);
  }

  @Nonnull
  public Page<ParticipantEntity> findAll(@Nonnull final Specification<ParticipantEntity> spec,
                                         @Nonnull final Pageable pageable) {
    return jpaRepository.findAll(spec, pageable);
  }

  public long count() {
    return jpaRepository.count();
  }

  public long count(@Nonnull final Specification<ParticipantEntity> spec) {
    return jpaRepository.count(spec);
  }


  public ParticipantEntity save(@Nonnull final ParticipantEntity participant) {
    return jpaRepository.save(participant);
  }

  public ParticipantEntity update(@Nonnull final ParticipantEntity participant) {
    if (participant.getId() != null) {
      return update(participant.getId(), participant);
    }

    return jpaRepository.save(participant);
  }

  public ParticipantEntity update(@Nonnull final Long id,
                                  @Nonnull final ParticipantEntity participant) {
    if (!jpaRepository.existsById(id)) {
      throw new IllegalStateException("Participant not found: " + id);
    }

    participant.setId(id);
    return jpaRepository.save(participant);
  }
}