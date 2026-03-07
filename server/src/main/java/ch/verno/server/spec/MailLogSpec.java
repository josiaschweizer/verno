package ch.verno.server.spec;

import ch.verno.common.db.filter.MailLogFilter;
import ch.verno.db.entity.mail.MailLogEntity;
import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.domain.Specification;

public class MailLogSpec extends BaseSpec<MailLogEntity, MailLogFilter> {


  @Override
  public Specification<MailLogEntity> getSpecification(@Nonnull final MailLogFilter mailLogFilter) {
    return null;
  }
}
