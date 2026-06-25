package ch.verno.server.spec;

import ch.verno.contract.dto.table.base.SortOrderDto;
import jakarta.annotation.Nonnull;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

public class PageHelper {

  @Nonnull
  public static PageRequest createPageRequest(final int offset,
                                              final int limit,
                                              @Nonnull final List<SortOrderDto> sortOrders) {
    final int page = offset / limit;

    final var sort = sortOrders.isEmpty()
            ? Sort.unsorted()
            : Sort.by(
            sortOrders.stream()
                    .map(order -> new Sort.Order(
                            order.ascending() ?
                            Sort.Direction.ASC :
                            Sort.Direction.DESC,
                            order.property()
                    ))
                    .toList()
    );

    return PageRequest.of(page, limit, sort);
  }

}
