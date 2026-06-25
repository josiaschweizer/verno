package ch.verno.server.bo.table.participant;

import ch.verno.contract.dto.table.participant.ParentDto;
import ch.verno.lib.Lazy;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.sanitize.participant.ParentSanitizer;
import ch.verno.server.service.intern.table.participant.ParentService;
import jakarta.annotation.Nonnull;

public class ParentBo {

  @Nonnull private final Lazy<ParentService> parentService;

  protected ParentBo(@Nonnull final ServerBean serverBean) {
    this.parentService = Lazy.of(() -> serverBean.get(ParentService.class));
  }

  @Nonnull
  public ParentDto findOrCreate(@Nonnull final ParentDto parent) {
    final var sanitizedDto = ParentSanitizer.sanitize(parent);

    final var foundOptional = parentService.get().findByFields(
            sanitizedDto.getFirstName(),
            sanitizedDto.getLastName(),
            sanitizedDto.getEmail(),
            sanitizedDto.getPhone()
    );
    return foundOptional.orElseGet(() -> parentService.get().save(sanitizedDto));
  }

}
