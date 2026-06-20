package ch.verno.contract.dto.file.storage;

import jakarta.annotation.Nullable;

public record FileDeleteResponse(boolean success,
                                 @Nullable String message) {
}
