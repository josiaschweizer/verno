package ch.verno.contract.dto.file.storage;

import jakarta.annotation.Nonnull;

public record StoredObjectDto(@Nonnull String key,
                              long size) {
}
