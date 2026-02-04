package ch.verno.server.repository;

import ch.verno.db.entity.ParentEntity;
import ch.verno.db.jpa.SpringDataParentJpaRepository;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ParentRepository {

  @Nonnull
  private final SpringDataParentJpaRepository springDataParentJpaRepository;

  public ParentRepository(@Nonnull final SpringDataParentJpaRepository springDataParentJpaRepository) {
    this.springDataParentJpaRepository = springDataParentJpaRepository;
  }

  @Nonnull
  public Optional<ParentEntity> findById(@Nonnull final Long id, @Nonnull final Long mandantId) {
    return springDataParentJpaRepository.findByIdAndMandant_Id(id, mandantId);
  }

  @Nonnull
  public List<ParentEntity> findAll(@Nonnull final Long mandantId) {
    return springDataParentJpaRepository.findAllByMandant_Id(mandantId);
  }

  @Nonnull
  public ParentEntity save(@Nonnull final ParentEntity entity) {
    return springDataParentJpaRepository.save(entity);
  }

  @Nonnull
  public Optional<ParentEntity> findByFields(@Nonnull final Long mandantId,
                                             @Nonnull final String firstname,
                                             @Nonnull final String lastname,
                                             @Nonnull final String email,
                                             @Nonnull final String phone) {
    return springDataParentJpaRepository.findByMandant_IdAndFirstnameAndLastnameAndEmailAndPhone(
            mandantId, firstname, lastname, email, phone
    );
  }
}