package ch.verno.common.api.dto.internal.file.storage;

import jakarta.annotation.Nullable;

public record FileDeleteResponse(boolean success,
                                 @Nullable String message) {
}
