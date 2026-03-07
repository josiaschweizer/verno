package ch.verno.server.spec;

import ch.verno.common.db.filter.MailLogFilter;
import ch.verno.db.entity.mail.MailLogEntity;
import jakarta.annotation.Nonnull;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;

public class MailLogSpec extends BaseSpec<MailLogEntity, MailLogFilter> {


  @Override
  public Specification<MailLogEntity> getSpecification(@Nonnull final MailLogFilter mailLogFilter) {
    return (root, query, cb) -> cb.and(new ArrayList<Predicate>().toArray(new Predicate[0]));
  }
}
