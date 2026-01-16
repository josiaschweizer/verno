package ch.verno.server.report.course;

import ch.verno.common.db.dto.table.CourseDto;
import ch.verno.common.db.dto.table.ParticipantDto;
import ch.verno.report.dto.CourseReportDto;
import ch.verno.server.report.participant.ParticipantReportMapper;
import jakarta.annotation.Nonnull;

import java.time.LocalDate;
import java.util.List;

public class CourseReportMapper {

  private CourseReportMapper() {
  }

  public static CourseReportDto map(@Nonnull final CourseDto course,
                                    @Nonnull final List<ParticipantDto> participants,
                                    @Nonnull final List<LocalDate> courseDate) {
    return new CourseReportDto(
            course.getTitle(),
            participants.stream()
                    .map(ParticipantReportMapper::map)
                    .toList(),
            course.getCapacity(),
            course.getCourseLevelAsString(),
            course.getCourseScheduleAsString(),
            courseDate,
            course.getStartTime(),
            course.getEndTime()
    );
  }


}
