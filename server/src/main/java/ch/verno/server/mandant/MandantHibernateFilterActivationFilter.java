package ch.verno.server.mandant;

import ch.verno.common.mandant.MandantContext;
import ch.verno.db.entity.mandant.MandantFilters;
import jakarta.annotation.Nonnull;
import jakarta.persistence.EntityManager;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.Session;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 50)
public class MandantHibernateFilterActivationFilter extends OncePerRequestFilter {

  private final EntityManager entityManager;

  public MandantHibernateFilterActivationFilter(final EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @Override
  protected void doFilterInternal(@Nonnull final HttpServletRequest request,
                                  @Nonnull final HttpServletResponse response,
                                  @Nonnull final FilterChain filterChain) throws ServletException, IOException {


    final var session = entityManager.unwrap(Session.class);
    session.enableFilter(MandantFilters.MANDANT_FILTER)
            .setParameter(MandantFilters.PARAM_MANDANT_ID, MandantContext.getRequired());

    try {
      filterChain.doFilter(request, response);
    } finally {
      // todo: Context cleanup, falls du es nicht schon woanders machst ch.verno.server.mandant.MandantFilter.doFilterInternal?
      // MandantContext.clear();
    }
  }
}