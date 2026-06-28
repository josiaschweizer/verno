package ch.verno.rpc.client.helper;


import ch.verno.contract.dto.table.base.SortOrderDto;

import com.vaadin.flow.data.provider.QuerySortOrder;

import com.vaadin.flow.data.provider.SortDirection;

import jakarta.annotation.Nonnull;

import java.util.List;

public final class SortOrderMapper {

  @Nonnull
  public static List<SortOrderDto> toDto(@Nonnull final List<QuerySortOrder> sortOrders) {
    return sortOrders.stream()
            .map(SortOrderMapper::toDto)
            .toList();
  }

  @Nonnull
  public static SortOrderDto toDto(@Nonnull final QuerySortOrder sortOrder) {
    return new SortOrderDto(
            sortOrder.getSorted(),
            sortOrder.getDirection() == SortDirection.ASCENDING
    );
  }

}