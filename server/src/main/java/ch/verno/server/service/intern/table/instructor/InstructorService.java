package ch.verno.server.service.intern.table.instructor;

import ch.verno.contract.dto.filter.InstructorFilter;
import ch.verno.contract.dto.table.instructor.InstructorDto;
import ch.verno.db.entity.instructor.InstructorEntity;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.mapper.instructor.InstructorMapper;
import ch.verno.server.repository.instructor.InstructorRepository;
import ch.verno.server.service.base.AbstractSpecEntityService;
import ch.verno.server.spec.InstructorSpec;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;

@Service
public class InstructorService extends AbstractSpecEntityService<
        Long,
        InstructorEntity,
        InstructorDto,
        InstructorRepository,
        InstructorMapper,
        InstructorSpec,
        InstructorFilter> {

  protected InstructorService(@Nonnull final ServerBean serverBean) {
    super(serverBean.get(InstructorRepository.class), serverBean.get(InstructorMapper.class), InstructorSpec::new);
  }
}
