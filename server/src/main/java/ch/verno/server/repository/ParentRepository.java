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
  private final SpringDataParentJpaRepository jpaRepository;

  public ParentRepository(@Nonnull final SpringDataParentJpaRepository jpaRepository) {
    this.jpaRepository = jpaRepository;
  }

  @Nonnull
  public Optional<ParentEntity> findById(@Nonnull final Long id, @Nonnull final Long tenantId) {
    return jpaRepository.findByIdAndTenant_Id(id, tenantId);
  }

  @Nonnull
  public List<ParentEntity> findAll(@Nonnull final Long tenantId) {
    return jpaRepository.findAllByTenant_Id(tenantId);
  }

  @Nonnull
  public ParentEntity save(@Nonnull final ParentEntity entity) {
    return jpaRepository.save(entity);
  }

  @Nonnull
  public Optional<ParentEntity> findByFields(@Nonnull final Long tenantId,
                                             @Nonnull final String firstname,
                                             @Nonnull final String lastname,
                                             @Nonnull final String email,
                                             @Nonnull final String phone) {
    return jpaRepository.findByTenant_IdAndFirstnameAndLastnameAndEmailAndPhone(
            tenantId, firstname, lastname, email, phone
    );
  }

  public void deleteById(@Nonnull final Long id) {
    jpaRepository.deleteById(id);
  }
}