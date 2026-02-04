package ch.verno.server.mandant;

import ch.verno.common.mandant.MandantContext;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class MandantFilterEnabler {

  @PersistenceContext
  private EntityManager entityManager;

  @Transactional
  public void enable() {
    final long mandantId = MandantContext.getRequired();

    Session session = entityManager.unwrap(Session.class);
    session.enableFilter("mandantFilter")
            .setParameter("mandantId", mandantId);
  }
}