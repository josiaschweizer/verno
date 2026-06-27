package ch.verno.contract.dto.response.base.save;

import ch.verno.contract.dto.response.base.Response;
import ch.verno.contract.dto.table.base.BaseDto;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

/**
 * Response for save actions on entities saved/updated via API or CSV imports.
 *
 * @param dto saved DTO on success, or {@code null} if no DTO is available on failure
 * @param successful whether the save succeeded
 * @param errorCode reason for failure, or {@code null} if successful
 * @param <T> type of DTO being saved
 */
public record SaveResponse<T extends BaseDto<?>>(@Nullable T dto,
                                                 boolean successful,
                                                 @Nullable SaveErrorCode errorCode) implements Response {

  /** Successful save. */
  @Nonnull
  public static <T extends BaseDto<?>> SaveResponse<T> success(@Nonnull final T dto) {
    return new SaveResponse<>(dto, true, null);
  }

  /** Failed save, no error code. */
  @Nonnull
  public static <T extends BaseDto<?>> SaveResponse<T> failed(@Nonnull final T dto) {
    return new SaveResponse<>(dto, false, null);
  }

  /** Failed save, no DTO available (e.g. entity not found). */
  @Nonnull
  public static <T extends BaseDto<?>> SaveResponse<T> failed(@Nonnull final SaveErrorCode saveErrorCode) {
    return new SaveResponse<>(null, false, saveErrorCode);
  }

  /** Failed save with both the attempted DTO and error code. */
  @Nonnull
  public static <T extends BaseDto<?>> SaveResponse<T> failed(@Nonnull final T dto,
                                                              @Nonnull final SaveErrorCode saveErrorCode) {
    return new SaveResponse<>(dto, false, saveErrorCode);
  }

}