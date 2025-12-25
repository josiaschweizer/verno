package ch.verno.server.service;

import ch.verno.common.db.dto.*;
import ch.verno.common.exceptions.NotFoundException;
import ch.verno.common.exceptions.NotFoundReason;
import ch.verno.common.util.Publ;
import ch.verno.db.entity.*;
import ch.verno.server.repository.*;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public class ServiceHelper {

  @Nullable
  public AddressEntity saveOrUpdateAddress(@Nonnull final AddressRepository addressRepository,
                                           @Nullable final AddressDto addressDto) {
    if (addressDto == null || addressDto.isEmpty()) {
      return null;
    }

    final AddressEntity entity;
    if (addressDto.getId() != null && addressDto.getId() != 0L) {
      entity = addressRepository.findById(addressDto.getId())
              .orElseThrow(() -> new NotFoundException(NotFoundReason.ADDRESS_BY_ID_NOT_FOUND, addressDto.getId()));

      entity.setStreet(safeString(addressDto.getStreet()));
      entity.setHouseNumber(safeString(addressDto.getHouseNumber()));
      entity.setZipCode(safeString(addressDto.getZipCode()));
      entity.setCity(safeString(addressDto.getCity()));
      entity.setCountry(safeString(addressDto.getCountry()));
    } else {
      entity = new AddressEntity(
              safeString(addressDto.getStreet()),
              safeString(addressDto.getHouseNumber()),
              safeString(addressDto.getZipCode()),
              safeString(addressDto.getCity()),
              safeString(addressDto.getCountry())
      );
    }

    return addressRepository.save(entity);
  }

  @Nullable
  public ParentEntity saveOrUpdateParent(@Nonnull final ParentRepository parentRepository,
                                         @Nonnull final GenderRepository genderRepository,
                                         @Nonnull final AddressRepository addressRepository,
                                         @Nullable final ParentDto parentDto) {
    if (parentDto == null || parentDto.isEmpty()) {
      return null;
    }

    final ParentEntity entity;
    if (parentDto.getId() != null && parentDto.getId() != 0) {
      entity = parentRepository.findById(parentDto.getId())
              .orElseThrow(() -> new NotFoundException(NotFoundReason.PARENT_BY_ID_NOT_FOUND, parentDto.getId()));
    } else {
      entity = new ParentEntity(
              safeString(parentDto.getFirstName()),
              safeString(parentDto.getLastName()),
              safeString(parentDto.getEmail()),
              !parentDto.getPhoneNumber().isEmpty()
                      ? parentDto.getPhoneNumber().toString()
                      : Publ.EMPTY_STRING
      );
    }

    entity.setFirstname(safeString(parentDto.getFirstName()));
    entity.setLastname(safeString(parentDto.getLastName()));
    entity.setEmail(safeString(parentDto.getEmail()));
    entity.setPhone(!parentDto.getPhoneNumber().isEmpty()
            ? parentDto.getPhoneNumber().toString()
            : Publ.EMPTY_STRING
    );

    entity.setGender(resolveGender(genderRepository, parentDto.getGender()));
    entity.setAddress(saveOrUpdateAddress(addressRepository, parentDto.getAddress()));

    return parentRepository.save(entity);
  }

  @Nullable
  public GenderEntity resolveGender(@Nonnull final GenderRepository genderRepository,
                                    @Nullable final GenderDto genderDto) {
    if (genderDto == null || genderDto.isEmpty() || genderDto.id() == null) {
      return null;
    }

    return genderRepository.findById(genderDto.id())
            .orElseThrow(() -> new NotFoundException(NotFoundReason.GENDER_BY_ID_NOT_FOUND, genderDto.id()));
  }

  @Nullable
  public CourseLevelEntity resolveCourseLevel(@Nonnull final CourseLevelRepository courseLevelRepository,
                                              @Nullable final CourseLevelDto dto) {
    if (dto == null || dto.isEmpty() || dto.id() == null) {
      return null;
    }

    return courseLevelRepository.findById(dto.id())
            .orElseThrow(() -> new NotFoundException(NotFoundReason.COURSE_LEVEL_BY_ID_NOT_FOUND, dto.id()));
  }

  @Nullable
  public CourseEntity resolveCourse(@Nonnull final CourseRepository courseRepository,
                                    @Nullable final CourseDto dto) {
    if (dto == null || dto.isEmpty() || dto.id() == null) {
      return null;
    }

    return courseRepository.findById(dto.id())
            .orElseThrow(() -> new NotFoundException(NotFoundReason.COURSE_BY_ID_NOT_FOUND, dto.id()));
  }

  @Nonnull
  public static String safeString(@Nullable final String value) {
    return value == null ? Publ.EMPTY_STRING : value;
  }

}
