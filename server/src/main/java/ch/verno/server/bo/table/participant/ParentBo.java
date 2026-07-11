package ch.verno.server.bo.table.participant;

import ch.verno.contract.dto.table.participant.ParentDto;
import ch.verno.lib.Lazy;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.sanitize.participant.ParentSanitizer;
import ch.verno.server.service.entity.participant.ParentService;
import ch.verno.server.util.ServerStringUtil;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.Optional;

public class ParentBo {

  @Nonnull private final Lazy<ParentService> parentService;

  protected ParentBo(@Nonnull final ServerBean serverBean) {
    this.parentService = Lazy.of(() -> serverBean.get(ParentService.class));
  }

  @Nonnull
  public ParentDto saveOrEmpty(@Nonnull final ParentDto parentDto) {
    return hasContent(parentDto) ?
            parentService.get().save(parentDto) :
            ParentDto.empty();
  }

  @Nonnull
  public ParentDto findOrCreate(@Nonnull final ParentDto parent) {
    if (!hasContent(parent)) {
      return ParentDto.empty();
    }

    final var sanitizedDto = ParentSanitizer.sanitize(parent);
    return findByFields(sanitizedDto).orElseGet(() -> parentService.get().save(sanitizedDto));
  }

  @Nonnull
  public Optional<ParentDto> findByFields(@Nonnull final ParentDto parent) {
    return parentService.get().findByFields(
            parent.getFirstName(),
            parent.getLastName(),
            parent.getEmail(),
            parent.getPhone()
    );
  }

  public boolean hasContent(@Nullable final ParentDto parentDto) {
    if (parentDto == null) {
      return false;
    }

    return !ServerStringUtil.safeString(parentDto.getFirstName()).isBlank() ||
            !ServerStringUtil.safeString(parentDto.getLastName()).isBlank() ||
            !ServerStringUtil.safeString(parentDto.getEmail()).isBlank() ||
            !ServerStringUtil.safeString(parentDto.getPhone().toString()).isBlank();
  }
}
