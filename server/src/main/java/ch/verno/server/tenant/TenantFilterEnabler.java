package ch.verno.server.tenant;

import ch.verno.common.tenant.TenantContext;
import ch.verno.db.entity.tenant.TenantFilters;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class TenantFilterEnabler {

  @PersistenceContext
  private EntityManager entityManager;

  @Transactional
  public void enable() {
    final var tenantId = TenantContext.getRequired();

    entityManager.unwrap(Session.class)
            .enableFilter(TenantFilters.TENANT_FILTER)
            .setParameter(TenantFilters.PARAM_TENANT_ID, tenantId);
  }
}