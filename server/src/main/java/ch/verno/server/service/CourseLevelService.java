package ch.verno.server.service;

import ch.verno.common.db.dto.CourseLevelDto;
import ch.verno.common.db.filter.CourseLevelFilter;
import ch.verno.common.db.service.ICourseLevelService;
import ch.verno.common.exceptions.db.DBNotFoundException;
import ch.verno.common.exceptions.db.DBNotFoundReason;
import ch.verno.server.mapper.CourseLevelMapper;
import ch.verno.server.repository.CourseLevelRepository;
import ch.verno.server.spec.CourseLevelSpec;
import com.vaadin.flow.data.provider.QuerySortOrder;
import com.vaadin.flow.data.provider.SortDirection;
import jakarta.annotation.Nonnull;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CourseLevelService implements ICourseLevelService {

  @Nonnull
  private final CourseLevelRepository courseLevelRepository;
  @Nonnull
  private final CourseLevelSpec courseLevelSpec;

  public CourseLevelService(@Nonnull final CourseLevelRepository courseLevelRepository) {
    this.courseLevelRepository = courseLevelRepository;

    this.courseLevelSpec = new CourseLevelSpec();
  }

  @Nonnull
  @Override
  @Transactional
  public CourseLevelDto createCourseLevel(@Nonnull final CourseLevelDto courseLevelDto) {
    final var entity = CourseLevelMapper.toEntity(courseLevelDto);
    if (entity == null) {
      throw new IllegalArgumentException("CourseLevelDto is empty");
    }

    entity.setId(null);

    final var saved = courseLevelRepository.save(entity);
    return CourseLevelMapper.toDto(saved);
  }

  @Nonnull
  @Override
  @Transactional
  public CourseLevelDto updateCourseLevel(@Nonnull final CourseLevelDto courseLevelDto) {
    if (courseLevelDto.getId() == null || courseLevelDto.getId() == 0) {
      throw new IllegalArgumentException("CourseLevel ID is required for update");
    }

    final var existing = courseLevelRepository.findById(courseLevelDto.getId())
            .orElseThrow(() -> new DBNotFoundException(
                    DBNotFoundReason.COURSE_LEVEL_BY_ID_NOT_FOUND,
                    courseLevelDto.getId()
            ));

    final var updated = CourseLevelMapper.toEntity(courseLevelDto);
    if (updated == null) {
      throw new IllegalArgumentException("CourseLevelDto is empty");
    }

    updated.setId(existing.getId());

    final var saved = courseLevelRepository.save(updated);
    return CourseLevelMapper.toDto(saved);
  }

  @Nonnull
  @Override
  @Transactional(readOnly = true)
  public CourseLevelDto getCourseLevelById(@Nonnull final Long id) {
    final var courseLevelOptional = courseLevelRepository.findById(id);
    if (courseLevelOptional.isEmpty()) {
      throw new DBNotFoundException(DBNotFoundReason.COURSE_LEVEL_BY_ID_NOT_FOUND, id);
    }

    return CourseLevelMapper.toDto(courseLevelOptional.get());
  }

  @Nonnull
  @Override
  @Transactional(readOnly = true)
  public List<CourseLevelDto> getAllCourseLevels() {
    return courseLevelRepository.findAll().stream().map(CourseLevelMapper::toDto).toList();
  }

  @Nonnull
  @Transactional(readOnly = true)
  public List<CourseLevelDto> findCourseLevels(@Nonnull final CourseLevelFilter filter,
                                               final int offset,
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

    final var pageable = PageRequest.of(page, limit, sort);
    final var spec = courseLevelSpec.courseLevelSpec(filter);

    return courseLevelRepository.findAll(spec, pageable).stream()
            .map(CourseLevelMapper::toDto)
            .toList();
  }

  @Transactional(readOnly = true)
  public int countCourses(@Nonnull final CourseLevelFilter filter) {
    return Math.toIntExact(courseLevelRepository.count(courseLevelSpec.courseLevelSpec(filter)));
  }
}
