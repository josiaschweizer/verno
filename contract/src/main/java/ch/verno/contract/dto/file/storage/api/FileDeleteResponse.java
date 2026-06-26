package ch.verno.contract.dto.file.storage.api;

import jakarta.annotation.Nullable;

public record FileDeleteResponse(boolean success,
                                 @Nullable String message) {
}
