package ch.verno.server.service.intern.table.instructor;

import ch.verno.contract.dto.table.instructor.InstructorDto;
import ch.verno.db.entity.instructor.InstructorEntity;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.mapper.db.instructor.InstructorMapper;
import ch.verno.server.repository.instructor.InstructorRepository;
import ch.verno.server.service.base.AbstractEntityService;
import jakarta.annotation.Nonnull;

public class InstructorService extends AbstractEntityService<
        InstructorEntity,
        InstructorDto,
        InstructorRepository,
        InstructorMapper> {

  protected InstructorService(@Nonnull final ServerBean serverBean) {
    super(serverBean.get(InstructorRepository.class), serverBean.get(InstructorMapper.class));
  }
}
