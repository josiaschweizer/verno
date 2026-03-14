package ch.verno.common.db.service.intern;

import ch.verno.common.db.dto.table.ParentDto;
import jakarta.annotation.Nonnull;

import java.util.List;

public interface IParentService {

  @Nonnull
  ParentDto createParent(@Nonnull final ParentDto parentDto);

  @Nonnull
  ParentDto updateParent(@Nonnull final ParentDto parentDto);

  @Nonnull
  ParentDto getParentById(@Nonnull final Long id);

  @Nonnull
  List<ParentDto> getParents();

}
