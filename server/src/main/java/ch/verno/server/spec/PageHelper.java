package ch.verno.server.spec;

import com.vaadin.flow.data.provider.QuerySortOrder;
import com.vaadin.flow.data.provider.SortDirection;
import jakarta.annotation.Nonnull;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

public class PageHelper {

  @Nonnull
  public static PageRequest createPageRequest(final int offset,
                                              final int limit,
                                              @Nonnull final List<QuerySortOrder> sortOrders) {
    final int page = offset / limit;

    final var sort = sortOrders.isEmpty()
            ? Sort.unsorted()
            : Sort.by(
            sortOrders.stream()
                    .map(order -> new Sort.Order(
                            order.getDirection() == SortDirection.ASCENDING
                                    ? Sort.Direction.ASC
                                    : Sort.Direction.DESC,
                            order.getSorted()
                    ))
                    .toList()
    );

    return PageRequest.of(page, limit, sort);
  }

}
