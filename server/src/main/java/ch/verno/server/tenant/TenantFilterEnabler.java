package ch.verno.server.tenant;

import ch.verno.common.tenant.TenantContext;
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
    final var tenantId = TenantContext.get();
    if (tenantId == null) {
      return;
    }

    final Session session = entityManager.unwrap(Session.class);
    session.enableFilter("mandantFilter")
            .setParameter("mandantId", tenantId);
  }

  @Transactional
  public void disable() {
    final Session session = entityManager.unwrap(Session.class);
    session.disableFilter("mandantFilter");
  }
}