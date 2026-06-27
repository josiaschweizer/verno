package ch.verno.contract.dto.response.base.delete;

import ch.verno.contract.dto.response.base.Response;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public record DeleteResponse(boolean successful,
                             @Nullable DeleteErrorCode deleteErrorCode) implements Response {

  @Nonnull
  public static DeleteResponse success() {
    return new DeleteResponse(true, null);
  }

  @Nonnull
  public static DeleteResponse faulty() {
    return new DeleteResponse(false, null);
  }

  @Nonnull
  public static DeleteResponse faulty(@Nullable final DeleteErrorCode deleteErrorCode) {
    return new DeleteResponse(false, deleteErrorCode);
  }

  @Nonnull
  public static DeleteResponse emptyId() {
    return DeleteResponse.faulty(DeleteErrorCode.ID_EMPTY);
  }

}
