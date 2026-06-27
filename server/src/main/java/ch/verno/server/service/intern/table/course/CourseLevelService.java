package ch.verno.server.service.intern.table.course;

import ch.verno.contract.dto.table.course.CourseLevelDto;
import ch.verno.db.entity.course.CourseLevelEntity;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.mapper.course.CourseLevelMapper;
import ch.verno.server.repository.course.CourseLevelRepository;
import ch.verno.server.service.base.AbstractEntityServiceLongId;
import jakarta.annotation.Nonnull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class CourseLevelService extends AbstractEntityServiceLongId<
        CourseLevelEntity,
        CourseLevelDto,
        CourseLevelRepository,
        CourseLevelMapper> {

  public CourseLevelService(@Nonnull final ServerBean serverBean) {
    super(serverBean.get(CourseLevelRepository.class), serverBean.get(CourseLevelMapper.class));
  }

  @Nonnull
  public Optional<CourseLevelDto> findByCode(@Nonnull final String code) {
    return getRepository().findByCode(code)
            .map(getMapper()::toSimpleDto);
  }

}
