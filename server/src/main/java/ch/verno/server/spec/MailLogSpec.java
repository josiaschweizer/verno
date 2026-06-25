package ch.verno.server.spec;

import ch.verno.contract.dto.filter.MailLogFilter;
import ch.verno.db.entity.mail.MailLogEntity;
import jakarta.annotation.Nonnull;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class MailLogSpec extends BaseSpec<MailLogEntity, MailLogFilter> {

  @Override
  public Specification<MailLogEntity> getSpecification(@Nonnull final MailLogFilter filter) {
    return (root, query, cb) -> {

      final List<Predicate> predicates = new ArrayList<>();

      final var searchText = normalize(filter.searchText());
      if (!searchText.isBlank()) {
        final var search = "%" + filter.searchText().toLowerCase() + "%";

        predicates.add(cb.or(
                cb.like(cb.lower(root.get("recipientEmail")), search),
                cb.like(cb.lower(root.get("recipientName")), search),
                cb.like(cb.lower(root.get("subject")), search),
                cb.like(cb.lower(root.get("templateName")), search),
                cb.like(cb.lower(root.get("content")), search),
                cb.like(cb.lower(root.get("providerMessageId")), search),
                cb.like(cb.lower(root.get("errorMessage")), search)
        ));
      }

      return cb.and(predicates.toArray(new Predicate[0]));
    };
  }
}