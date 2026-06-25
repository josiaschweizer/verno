package ch.verno.contract.dto.result.base;

import ch.verno.contract.dto.table.base.BaseDto;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public record SaveResult<T extends BaseDto>(@Nullable T dto,
                                            boolean successful,
                                            @Nullable String errorCode,
                                            @Nullable String errorMessage) {

  @Nonnull
  public static <T extends BaseDto> SaveResult<T> success(@Nonnull final T dto) {
    return new SaveResult<>(dto, true, null, null);
  }

  @Nonnull
  public static <T extends BaseDto> SaveResult<T> failed(@Nonnull final T dto) {
    return new SaveResult<>(dto, false, null, null);
  }

  @Nonnull
  public static <T extends BaseDto> SaveResult<T> failed(@Nonnull final String errorCode) {
    return new SaveResult<>(null, false, errorCode, null);
  }

  @Nonnull
  public static <T extends BaseDto> SaveResult<T> failed(@Nonnull final String errorCode,
                                                         @Nonnull final String errorMessage) {
    return new SaveResult<>(null, false, errorCode, errorMessage);
  }

  @Nonnull
  public static <T extends BaseDto> SaveResult<T> failed(@Nonnull final T dto,
                                                         @Nonnull final String errorCode,
                                                         @Nonnull final String errorMessage) {
    return new SaveResult<>(dto, false, errorCode, errorMessage);
  }

}
