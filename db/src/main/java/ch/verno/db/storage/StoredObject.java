package ch.verno.db.storage;

import jakarta.annotation.Nonnull;

public record StoredObject(@Nonnull String key,
                           long size) {
}