package ch.verno.server.bo.table.instructor;

import ch.verno.contract.dto.response.base.save.SaveErrorCode;
import ch.verno.contract.dto.response.base.save.SaveResponse;
import ch.verno.contract.dto.table.instructor.InstructorDto;
import ch.verno.lib.Lazy;
import ch.verno.server.bean.ServerBean;
import ch.verno.server.service.entity.course.CourseService;
import ch.verno.server.service.entity.instructor.InstructorService;
import jakarta.annotation.Nonnull;
import org.jetbrains.annotations.NonNls;
import org.springframework.dao.DataIntegrityViolationException;

public class InstructorBo {

  @NonNls public static final String DUPLICATE_KEY_VALUE_VIOLATES_UNIQUE_CONSTRAINT = "duplicate key value violates unique constraint";
  @NonNls public static final String EMAIL = "email";

  @Nonnull private final Lazy<CourseService> courseService;
  @Nonnull private final Lazy<InstructorService> instructorService;

  protected InstructorBo(@Nonnull final ServerBean serverBean) {
    this.courseService = Lazy.of(() -> serverBean.get(CourseService.class));
    this.instructorService = Lazy.of(() -> serverBean.get(InstructorService.class));
  }

  @Nonnull
  public SaveResponse<InstructorDto> saveInstructor(@Nonnull final InstructorDto instructorDto) {
    try {
      final var saved = instructorService.get().save(instructorDto);
      instructorService.get().flush();

      return SaveResponse.success(saved);
    } catch (DataIntegrityViolationException exception) {
      if (isDuplicatedInstructorEmail(exception)) {
        return SaveResponse.failed(SaveErrorCode.EMAIL_ALREADY_EXISTS);
      }

      return SaveResponse.failed(SaveErrorCode.DATABASE_ERROR);
    }
  }

  private boolean isDuplicatedInstructorEmail(@Nonnull final DataIntegrityViolationException exception) {
    final var cause = exception.getMostSpecificCause();
    final var message = cause.getMessage();

    return message != null &&
            message.contains(DUPLICATE_KEY_VALUE_VIOLATES_UNIQUE_CONSTRAINT) &&
            message.toLowerCase().contains(EMAIL);
  }

  public boolean isInstructorReferenced(@Nonnull final Long instructorId) {
    if (!instructorService.get().existsById(instructorId)) {
      return false;
    }

    return courseService.get().existsById(instructorId);
  }

}
